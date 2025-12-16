package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.model.TransmisionType
import edu.ucne.kias_rent_car.presentation.Components.CategoryChip
import edu.ucne.kias_rent_car.presentation.Components.KiaBottomNavigation
import edu.ucne.kias_rent_car.presentation.Components.KiaSearchBar
import edu.ucne.kias_rent_car.presentation.Components.VehicleCard
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimDark

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToVehicleDetail: (String) -> Unit,
    onNavigateToBookings: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is HomeUiEvent.OnVehicleClicked -> onNavigateToVehicleDetail(event.vehicleId)
                HomeUiEvent.NavigateToBookings -> onNavigateToBookings()
                HomeUiEvent.NavigateToSupport -> onNavigateToSupport()
                HomeUiEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            onEvent(HomeUiEvent.OnErrorDismissed)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KIA'S RENT CAR",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimDark)
            )
        },
        bottomBar = {
            KiaBottomNavigation(
                currentRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "bookings" -> onEvent(HomeUiEvent.NavigateToBookings)
                        "support" -> onEvent(HomeUiEvent.NavigateToSupport)
                        "profile" -> onEvent(HomeUiEvent.NavigateToProfile)
                        else -> Unit
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = scrimDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            KiaSearchBar(
                query = state.searchQuery,
                onQueryChange = { onEvent(HomeUiEvent.OnSearchQueryChanged(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(VehicleCategory.entries.toTypedArray()) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = state.selectedCategory == category,
                        onClick = { onEvent(HomeUiEvent.OnCategorySelected(category)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = onErrorDark)
                    }
                }
                state.vehicles.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay vehículos disponibles",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(items = state.vehicles, key = { it.id }) { vehicle ->
                            VehicleCard(
                                vehicle = vehicle,
                                onClick = { onEvent(HomeUiEvent.OnVehicleClicked(vehicle.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun HomeBodyPreview() {
    MaterialTheme {
        val state = HomeUiState(
            vehicles = listOf(
                Vehicle("1", 1, "Kia Sportage", "SUV", VehicleCategory.SUV, 5, TransmisionType.AUTOMATIC, 120.0, "", true),
                Vehicle("2", 2, "Kia K5", "Sedan", VehicleCategory.SEDAN, 5, TransmisionType.AUTOMATIC, 100.0, "", true)
            ),
            selectedCategory = VehicleCategory.ALL
        )
        HomeBody(state) {}
    }
}
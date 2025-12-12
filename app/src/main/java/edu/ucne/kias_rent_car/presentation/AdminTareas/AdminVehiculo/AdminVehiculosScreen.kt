package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.model.TransmisionType
import edu.ucne.kias_rent_car.presentation.Components.AdminBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun AdminVehiculosScreen(
    viewModel: AdminVehiculosViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddVehiculo: () -> Unit,
    onNavigateToEditVehiculo: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminVehiculosBody(
        state = state,
        onEvent = { event ->
            when (event) {
                AdminVehiculosUiEvent.NavigateBack -> onNavigateBack()
                AdminVehiculosUiEvent.NavigateToAddVehiculo -> onNavigateToAddVehiculo()
                is AdminVehiculosUiEvent.EditVehiculo -> onNavigateToEditVehiculo(event.id)
                AdminVehiculosUiEvent.NavigateToHome -> onNavigateToHome()
                AdminVehiculosUiEvent.NavigateToReservas -> onNavigateToReservas()
                AdminVehiculosUiEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminVehiculosBody(
    state: AdminVehiculosUiState,
    onEvent: (AdminVehiculosUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AdminVehiculosUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(AdminVehiculosUiEvent.NavigateToAddVehiculo) }, containerColor = onErrorDark) {
                Icon(Icons.Default.Add, "Agregar", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        bottomBar = {
            AdminBottomNavigation(
                currentRoute = "admin_vehiculos",
                onNavigate = { route ->
                    when (route) {
                        "admin_home" -> onEvent(AdminVehiculosUiEvent.NavigateToHome)
                        "admin_reservas" -> onEvent(AdminVehiculosUiEvent.NavigateToReservas)
                        "admin_vehiculos" -> { }
                        "admin_profile" -> onEvent(AdminVehiculosUiEvent.NavigateToProfile)
                    }
                }
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Gestión de Vehículos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onEvent(AdminVehiculosUiEvent.OnSearchChange(it)) },
                placeholder = { Text("Buscar por modelo", color = MaterialTheme.colorScheme.outline) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = onErrorDark,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = onErrorDark)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.vehiculos) { vehiculo ->
                        AdminVehiculoCard(
                            vehiculo = vehiculo,
                            onEdit = { onEvent(AdminVehiculosUiEvent.EditVehiculo(vehiculo.id)) },
                            onDelete = { onEvent(AdminVehiculosUiEvent.DeleteVehiculo(vehiculo.id)) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun AdminVehiculoCard(vehiculo: Vehicle, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(vehiculo.imagenUrl).crossfade(true).build(),
                contentDescription = vehiculo.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                loading = { Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) },
                error = {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.DirectionsCar, null, tint = MaterialTheme.colorScheme.outline)
                    }
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(vehiculo.modelo, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("Matrícula: ABC-123", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                Text(
                    if (vehiculo.disponible) "Disponible" else "No disponible",
                    fontSize = 12.sp,
                    color = if (vehiculo.disponible) MaterialTheme.colorScheme.primary else onErrorDark
                )
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.onSurface) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Eliminar", tint = onErrorDark) }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AdminVehiculosBodyPreview() {
    val state = AdminVehiculosUiState(
        vehiculos = listOf(
            Vehicle("1", 1, "Kia Sportage", "SUV", VehicleCategory.SUV, 5, TransmisionType.AUTOMATIC, 120.0, "", true)
        )
    )
    AdminVehiculosBody(state) {}
}
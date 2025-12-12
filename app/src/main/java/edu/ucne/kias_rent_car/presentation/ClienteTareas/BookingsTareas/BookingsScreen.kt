package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
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
import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.presentation.Components.KiaBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun BookingsScreen(
    viewModel: BookingsViewModel = hiltViewModel(),
    onNavigateToBookingDetail: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookingsBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is BookingsUiEvent.NavigateToDetail -> onNavigateToBookingDetail(event.bookingId)
                BookingsUiEvent.NavigateToHome -> onNavigateToHome()
                BookingsUiEvent.NavigateToSupport -> onNavigateToSupport()
                BookingsUiEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsBody(
    state: BookingsUiState,
    onEvent: (BookingsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        bottomBar = {
            KiaBottomNavigation(
                currentRoute = "bookings",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onEvent(BookingsUiEvent.NavigateToHome)
                        "bookings" -> { }
                        "support" -> onEvent(BookingsUiEvent.NavigateToSupport)
                        "profile" -> onEvent(BookingsUiEvent.NavigateToProfile)
                    }
                }
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Reservas", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.filtro == BookingFilter.ACTUALES,
                    onClick = { onEvent(BookingsUiEvent.FilterChanged(BookingFilter.ACTUALES)) },
                    label = { Text("Actuales") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = onErrorDark,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                FilterChip(
                    selected = state.filtro == BookingFilter.PASADAS,
                    onClick = { onEvent(BookingsUiEvent.FilterChanged(BookingFilter.PASADAS)) },
                    label = { Text("Pasadas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = onErrorDark,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = onErrorDark)
                    }
                }
                state.reservaciones.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tienes reservaciones", color = MaterialTheme.colorScheme.outline)
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.reservaciones) { reservacion ->
                            BookingCard(
                                reservacion = reservacion,
                                onClick = { onEvent(BookingsUiEvent.NavigateToDetail(reservacion.reservacionId)) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingCard(reservacion: Reservacion, onClick: () -> Unit) {
    val estadoColor = when (reservacion.estado) {
        EstadoReserva.CONFIRMADA, EstadoReserva.CONFIRMADA -> MaterialTheme.colorScheme.primary
        EstadoReserva.PENDIENTE -> MaterialTheme.colorScheme.tertiary
        EstadoReserva.CANCELADA, EstadoReserva.CANCELADA -> onErrorDark
        else -> MaterialTheme.colorScheme.outline
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(reservacion.vehiculo?.imagenUrl).crossfade(true).build(),
                contentDescription = reservacion.vehiculo?.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                loading = {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = onErrorDark, modifier = Modifier.size(20.dp))
                    }
                },
                error = {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.DirectionsCar, null, tint = MaterialTheme.colorScheme.outline)
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(reservacion.estado, fontSize = 12.sp, color = estadoColor, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(reservacion.vehiculo?.modelo ?: "", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("${reservacion.fechaRecogida} - ${reservacion.fechaDevolucion}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (reservacion.estado == EstadoReserva.PENDIENTE) "Gestionar" else "Ver Detalles", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BookingsBodyPreview() {
    MaterialTheme {
        val state = BookingsUiState(filtro = BookingFilter.ACTUALES)
        BookingsBody(state) {}
    }
}
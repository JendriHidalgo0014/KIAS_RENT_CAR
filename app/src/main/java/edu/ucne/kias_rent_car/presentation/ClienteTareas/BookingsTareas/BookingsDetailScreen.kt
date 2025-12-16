package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.domain.model.BookingDetailNavigation
import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.presentation.Components.*
import edu.ucne.kias_rent_car.ui.theme.onErrorDark

@Composable
fun BookingDetailScreen(
    viewModel: BookingDetailViewModel = hiltViewModel(),
    bookingId: String,
    navigation: BookingDetailNavigation
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId) {
        viewModel.onEvent(BookingDetailUiEvent.LoadBooking(bookingId))
    }

    BookingDetailBody(
        state = state,
        bookingId = bookingId,
        onEvent = { event ->
            when (event) {
                BookingDetailUiEvent.NavigateBack -> navigation.onNavigateBack()
                is BookingDetailUiEvent.NavigateToModify -> navigation.onNavigateToModify(event.bookingId)
                BookingDetailUiEvent.NavigateToHome -> navigation.onNavigateToHome()
                BookingDetailUiEvent.NavigateToBookings -> navigation.onNavigateToBookings()
                BookingDetailUiEvent.NavigateToSupport -> navigation.onNavigateToSupport()
                BookingDetailUiEvent.NavigateToProfile -> navigation.onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun BookingDetailBody(
    state: BookingDetailUiState,
    bookingId: String,
    onEvent: (BookingDetailUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(BookingDetailUiEvent.NavigateBack) },
        bottomBar = {
            KiaBottomNavigation(
                currentRoute = "bookings",
                onNavigate = { route -> handleNavigation(route, onEvent) }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            KiaLoadingBox(padding)
        } else {
            BookingDetailContent(state, bookingId, onEvent, padding)
        }
    }
}

private fun handleNavigation(route: String, onEvent: (BookingDetailUiEvent) -> Unit) {
    when (route) {
        "home" -> onEvent(BookingDetailUiEvent.NavigateToHome)
        "bookings" -> onEvent(BookingDetailUiEvent.NavigateToBookings)
        "support" -> onEvent(BookingDetailUiEvent.NavigateToSupport)
        "profile" -> onEvent(BookingDetailUiEvent.NavigateToProfile)
        else -> Unit
    }
}

@Composable
private fun BookingDetailContent(
    state: BookingDetailUiState,
    bookingId: String,
    onEvent: (BookingDetailUiEvent) -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        KiaVehicleImage(
            imageUrl = state.reservacion?.vehiculo?.imagenUrl,
            contentDescription = state.reservacion?.vehiculo?.modelo
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            VehicleInfoSection(state)
            ReservationDetailsSection(state)
            PriceBreakdownSection(state)
            ActionButtonsSection(state, bookingId, onEvent)
        }
    }
}

@Composable
private fun VehicleInfoSection(state: BookingDetailUiState) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        state.reservacion?.vehiculo?.modelo ?: "",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        state.reservacion?.vehiculo?.categoria?.displayName ?: "",
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.outline
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        state.reservacion?.estado ?: "",
        fontSize = 14.sp,
        color = getEstadoColor(state.reservacion?.estado),
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun getEstadoColor(estado: String?): Color {
    return when (estado) {
        EstadoReserva.CONFIRMADA -> MaterialTheme.colorScheme.primary
        EstadoReserva.PENDIENTE -> MaterialTheme.colorScheme.tertiary
        EstadoReserva.CANCELADA -> onErrorDark
        else -> MaterialTheme.colorScheme.outline
    }
}

@Composable
private fun ReservationDetailsSection(state: BookingDetailUiState) {
    KiaDetailRow(
        Icons.Default.CalendarMonth,
        "Recogida",
        "${state.reservacion?.fechaRecogida} - ${state.reservacion?.horaRecogida}"
    )
    Spacer(modifier = Modifier.height(12.dp))

    KiaDetailRow(
        Icons.Default.CalendarMonth,
        "Devolución",
        "${state.reservacion?.fechaDevolucion} - ${state.reservacion?.horaDevolucion}"
    )
    Spacer(modifier = Modifier.height(12.dp))

    KiaDetailRow(
        Icons.Default.LocationOn,
        "Lugar de Recogida",
        state.reservacion?.ubicacionRecogida?.nombre ?: ""
    )
    Spacer(modifier = Modifier.height(12.dp))

    KiaDetailRow(
        Icons.Default.LocationOn,
        "Lugar de Devolución",
        state.reservacion?.ubicacionDevolucion?.nombre ?: ""
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun PriceBreakdownSection(state: BookingDetailUiState) {
    Text(
        "Desglose de Precio",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    KiaPriceRow("Subtotal", "$${String.format("%.2f", state.reservacion?.subtotal ?: 0.0)}")
    KiaPriceRow("Impuestos", "$${String.format("%.2f", state.reservacion?.impuestos ?: 0.0)}")

    KiaPriceTotalRow(total = state.reservacion?.total ?: 0.0)

    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun ActionButtonsSection(
    state: BookingDetailUiState,
    bookingId: String,
    onEvent: (BookingDetailUiEvent) -> Unit
) {
    val canModify = state.reservacion?.estado in listOf(EstadoReserva.CONFIRMADA, EstadoReserva.PENDIENTE)

    if (canModify) {
        Button(
            onClick = { onEvent(BookingDetailUiEvent.NavigateToModify(bookingId)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Modificar Reserva", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { onEvent(BookingDetailUiEvent.CancelarReserva) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar Reserva", color = onErrorDark)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BookingDetailBodyPreview() {
    MaterialTheme {
        BookingDetailBody(BookingDetailUiState(), "1", onEvent = {})
    }
}
package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import edu.ucne.kias_rent_car.domain.model.BookingDetailNavigation
import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.presentation.Components.KiaBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailBody(
    state: BookingDetailUiState,
    bookingId: String,
    onEvent: (BookingDetailUiEvent) -> Unit
) {
    Scaffold(
        topBar = { BookingDetailTopBar(onEvent) },
        bottomBar = { BookingDetailBottomBar(onEvent) },
        containerColor = scrimLight
    ) { padding ->
        if (state.isLoading) {
            LoadingContent(padding)
        } else {
            BookingDetailContent(state, bookingId, onEvent, padding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingDetailTopBar(onEvent: (BookingDetailUiEvent) -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "KIA'S RENT CAR",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = { onEvent(BookingDetailUiEvent.NavigateBack) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
    )
}

@Composable
private fun BookingDetailBottomBar(onEvent: (BookingDetailUiEvent) -> Unit) {
    KiaBottomNavigation(
        currentRoute = "bookings",
        onNavigate = { route ->
            when (route) {
                "home" -> onEvent(BookingDetailUiEvent.NavigateToHome)
                "bookings" -> onEvent(BookingDetailUiEvent.NavigateToBookings)
                "support" -> onEvent(BookingDetailUiEvent.NavigateToSupport)
                "profile" -> onEvent(BookingDetailUiEvent.NavigateToProfile)
                else -> Unit
            }
        }
    )
}

@Composable
private fun LoadingContent(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = onErrorDark)
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
        VehicleImage(state.reservacion?.vehiculo?.imagenUrl)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            VehicleInfoSection(state)
            Spacer(modifier = Modifier.height(20.dp))
            ReservationDetailsSection(state)
            Spacer(modifier = Modifier.height(24.dp))
            PriceSection(state)
            Spacer(modifier = Modifier.height(32.dp))
            ActionButtons(state, bookingId, onEvent)
        }
    }
}

@Composable
private fun VehicleImage(imageUrl: String?) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = onErrorDark)
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    )
}

@Composable
private fun VehicleInfoSection(state: BookingDetailUiState) {
    Text(
        text = state.reservacion?.vehiculo?.modelo ?: "",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            state.reservacion?.vehiculo?.categoria?.displayName ?: "",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
        Text("•", color = MaterialTheme.colorScheme.outline)
        Text(
            "${state.reservacion?.vehiculo?.asientos ?: 0} Asientos",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
        Text("•", color = MaterialTheme.colorScheme.outline)
        Text(
            state.reservacion?.vehiculo?.transmision?.displayName ?: "",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    val estadoColor = getEstadoColor(state.reservacion?.estado)
    Text(
        state.reservacion?.estado ?: "",
        fontSize = 14.sp,
        color = estadoColor,
        fontWeight = FontWeight.Bold
    )
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
    DetailRow(
        Icons.Default.CalendarMonth,
        "Recogida",
        "${state.reservacion?.fechaRecogida} - ${state.reservacion?.horaRecogida}"
    )
    Spacer(modifier = Modifier.height(12.dp))
    DetailRow(
        Icons.Default.CalendarMonth,
        "Devolución",
        "${state.reservacion?.fechaDevolucion} - ${state.reservacion?.horaDevolucion}"
    )
    Spacer(modifier = Modifier.height(12.dp))
    DetailRow(
        Icons.Default.LocationOn,
        "Lugar de Recogida",
        state.reservacion?.ubicacionRecogida?.nombre ?: ""
    )
    Spacer(modifier = Modifier.height(12.dp))
    DetailRow(
        Icons.Default.LocationOn,
        "Lugar de Devolución",
        state.reservacion?.ubicacionDevolucion?.nombre ?: ""
    )
}

@Composable
private fun PriceSection(state: BookingDetailUiState) {
    Text(
        "Desglose de Precio",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    PriceDetailRow("Subtotal", "$${String.format("%.2f", state.reservacion?.subtotal ?: 0.0)}")
    PriceDetailRow("Impuestos", "$${String.format("%.2f", state.reservacion?.impuestos ?: 0.0)}")

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Total",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            "$${String.format("%.2f", state.reservacion?.total ?: 0.0)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = onErrorDark
        )
    }
}

@Composable
private fun ActionButtons(
    state: BookingDetailUiState,
    bookingId: String,
    onEvent: (BookingDetailUiEvent) -> Unit
) {
    val canModify = state.reservacion?.estado in listOf(
        EstadoReserva.CONFIRMADA,
        EstadoReserva.PENDIENTE
    )

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

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = onErrorDark, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            Text(value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun PriceDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
        Text(value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BookingDetailBodyPreview() {
    MaterialTheme {
        val state = BookingDetailUiState()
        BookingDetailBody(state, "1") {}
    }
}
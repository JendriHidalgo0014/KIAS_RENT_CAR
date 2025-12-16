package edu.ucne.kias_rent_car.presentation.ReservationTareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.presentation.Components.*

@Composable
fun ReservationConfirmationScreen(
    viewModel: ReservationConfirmationViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.onEvent(ReservationConfirmationUiEvent.LoadData(vehicleId))
    }

    ReservationConfirmationBody(
        state = state,
        vehicleId = vehicleId,
        onEvent = { event ->
            when (event) {
                ReservationConfirmationUiEvent.NavigateBack -> onNavigateBack()
                is ReservationConfirmationUiEvent.ConfirmarReserva -> onConfirm(event.vehicleId)
                ReservationConfirmationUiEvent.Cancelar -> onCancel()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ReservationConfirmationBody(
    state: ReservationConfirmationUiState,
    vehicleId: String,
    onEvent: (ReservationConfirmationUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(ReservationConfirmationUiEvent.NavigateBack) }
    ) { padding ->
        if (state.isLoading) {
            KiaLoadingBox(padding)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirmación de Reserva",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                KiaVehicleImage(
                    imageUrl = state.vehicle?.imagenUrl,
                    contentDescription = state.vehicle?.modelo,
                    height = 180.dp,
                    cornerRadius = 12.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    state.vehicle?.modelo ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    state.vehicle?.categoria?.displayName ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(24.dp))

                ReservationDetailsSection(state)

                Spacer(modifier = Modifier.height(24.dp))

                PriceSummarySection(state)

                Spacer(modifier = Modifier.height(32.dp))

                KiaPrimaryButton(
                    text = "Confirmar Reserva",
                    onClick = { onEvent(ReservationConfirmationUiEvent.ConfirmarReserva(vehicleId)) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { onEvent(ReservationConfirmationUiEvent.Cancelar) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", fontSize = 16.sp, color = MaterialTheme.colorScheme.outline)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ReservationDetailsSection(state: ReservationConfirmationUiState) {
    KiaDetailRow(Icons.Default.CalendarMonth, "Recogida", state.fechaRecogida)
    Spacer(modifier = Modifier.height(12.dp))
    KiaDetailRow(Icons.Default.CalendarMonth, "Devolución", state.fechaDevolucion)
    Spacer(modifier = Modifier.height(12.dp))
    KiaDetailRow(Icons.Default.LocationOn, "Lugar de Recogida", state.lugarRecogida)
    Spacer(modifier = Modifier.height(12.dp))
    KiaDetailRow(Icons.Default.LocationOn, "Lugar de Devolución", state.lugarDevolucion)
}

@Composable
private fun PriceSummarySection(state: ReservationConfirmationUiState) {
    Text(
        "Resumen de Precio",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    KiaPriceRow("Subtotal (${state.dias} días)", "$${String.format("%.2f", state.subtotal)}")
    KiaPriceRow("Impuestos y tasas", "$${String.format("%.2f", state.impuestos)}")

    KiaPriceTotalRow(total = state.total)
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ReservationConfirmationBodyPreview() {
    MaterialTheme {
        ReservationConfirmationBody(
            state = ReservationConfirmationUiState(
                fechaRecogida = "15/01/2025 - 10:00",
                fechaDevolucion = "20/01/2025 - 10:00",
                lugarRecogida = "Aeropuerto SDQ",
                lugarDevolucion = "Aeropuerto SDQ",
                dias = 5,
                subtotal = 500.0,
                impuestos = 90.0,
                total = 590.0
            ),
            vehicleId = "1",
            onEvent = {}
        )
    }
}
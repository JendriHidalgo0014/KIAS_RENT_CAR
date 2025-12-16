package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.presentation.Components.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ModifyBookingScreen(
    viewModel: ModifyBookingViewModel = hiltViewModel(),
    bookingId: String,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId) {
        viewModel.onEvent(ModifyBookingUiEvent.LoadBooking(bookingId))
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onSaveSuccess()
    }

    ModifyBookingBody(
        state = state,
        onEvent = { event ->
            when (event) {
                ModifyBookingUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ModifyBookingBody(
    state: ModifyBookingUiState,
    onEvent: (ModifyBookingUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(ModifyBookingUiEvent.NavigateBack) }
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
                    "Modificar Reserva",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                KiaUbicacionDropdown(
                    label = "Lugar de Recogida",
                    selectedUbicacion = state.ubicacionRecogida,
                    ubicaciones = state.ubicaciones,
                    placeholder = "Selecciona lugar de recogida",
                    onUbicacionSelected = { onEvent(ModifyBookingUiEvent.OnUbicacionRecogidaChange(it)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                KiaUbicacionDropdown(
                    label = "Lugar de Devolución",
                    selectedUbicacion = state.ubicacionDevolucion,
                    ubicaciones = state.ubicaciones,
                    placeholder = "Selecciona lugar de devolución",
                    onUbicacionSelected = { onEvent(ModifyBookingUiEvent.OnUbicacionDevolucionChange(it)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                KiaDateTimeField(
                    label = "Fecha de Recogida",
                    value = state.fechaRecogida,
                    onDateTimeSelected = { fecha, hora ->
                        onEvent(ModifyBookingUiEvent.OnFechaRecogidaChange(fecha, hora))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                KiaDateTimeField(
                    label = "Fecha de Devolución",
                    value = state.fechaDevolucion,
                    onDateTimeSelected = { fecha, hora ->
                        onEvent(ModifyBookingUiEvent.OnFechaDevolucionChange(fecha, hora))
                    }
                )

                state.error?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                KiaPrimaryButton(
                    text = "Guardar Cambios",
                    onClick = { onEvent(ModifyBookingUiEvent.GuardarCambios) },
                    isLoading = state.isLoading
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ModifyBookingBodyPreview() {
    MaterialTheme {
        ModifyBookingBody(
            ModifyBookingUiState(
                fechaRecogida = "2025-01-15 - 10:00",
                fechaDevolucion = "2025-01-20 - 10:00"
            )
        ) {}
    }
}
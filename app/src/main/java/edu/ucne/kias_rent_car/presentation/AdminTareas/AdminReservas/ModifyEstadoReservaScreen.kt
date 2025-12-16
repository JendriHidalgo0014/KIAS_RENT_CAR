package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.presentation.Components.KiaPrimaryButton
import edu.ucne.kias_rent_car.presentation.Components.KiaScaffold
import edu.ucne.kias_rent_car.ui.theme.onErrorDark

@Composable
fun ModifyEstadoReservaScreen(
    viewModel: ModifyEstadoReservaViewModel = hiltViewModel(),
    reservacionId: String,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(reservacionId) {
        viewModel.onEvent(ModifyEstadoReservaUiEvent.LoadReservacion(reservacionId))
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onSaveSuccess()
    }

    ModifyEstadoReservaBody(
        state = state,
        onEvent = { event ->
            when (event) {
                ModifyEstadoReservaUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ModifyEstadoReservaBody(
    state: ModifyEstadoReservaUiState,
    onEvent: (ModifyEstadoReservaUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(ModifyEstadoReservaUiEvent.NavigateBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Modificar Estado de\nReserva",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoRow("ID de Reserva", state.codigoReserva)
            InfoRow("Nombre del Cliente", state.nombreCliente)
            InfoRow("Vehículo", state.vehiculo)
            InfoRow("Periodo de Alquiler", state.periodo)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Seleccionar Nuevo Estado",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            EstadoReserva.OPCIONES_MODIFICAR.forEach { estado ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.estadoSeleccionado == estado,
                        onClick = { onEvent(ModifyEstadoReservaUiEvent.OnEstadoChange(estado)) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = onErrorDark,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = estado,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            KiaPrimaryButton(
                text = "Guardar Cambios",
                onClick = { onEvent(ModifyEstadoReservaUiEvent.GuardarCambios) },
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ModifyEstadoReservaBodyPreview() {
    MaterialTheme {
        ModifyEstadoReservaBody(
            ModifyEstadoReservaUiState(
                codigoReserva = "KR-123456",
                nombreCliente = "Juan Pérez",
                vehiculo = "Kia Sportage",
                periodo = "15/01 - 20/01/2025"
            )
        ) {}
    }
}
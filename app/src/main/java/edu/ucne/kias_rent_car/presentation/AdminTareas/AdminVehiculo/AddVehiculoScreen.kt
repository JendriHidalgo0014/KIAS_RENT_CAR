package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.presentation.Components.*
import java.time.format.DateTimeFormatter

@Composable
fun AddVehiculoScreen(
    viewModel: AddVehiculoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onSuccess()
    }

    AddVehiculoBody(
        state = state,
        onEvent = { event ->
            when (event) {
                AddVehiculoUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun AddVehiculoBody(
    state: AddVehiculoUiState,
    onEvent: (AddVehiculoUiEvent) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    KiaScaffold(
        onNavigateBack = { onEvent(AddVehiculoUiEvent.NavigateBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Agregar Vehículo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            KiaTextField(
                value = state.modelo,
                onValueChange = { onEvent(AddVehiculoUiEvent.OnModeloChange(it)) },
                label = "Modelo",
                placeholder = "Ej. Kia Rio"
            )

            Spacer(modifier = Modifier.height(16.dp))

            KiaTextField(
                value = state.descripcion,
                onValueChange = { onEvent(AddVehiculoUiEvent.OnDescripcionChange(it)) },
                label = "Descripción",
                placeholder = "Añade una descripción",
                singleLine = false,
                minHeight = 100.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KiaDropdown(
                    value = state.categoria,
                    label = "Categoría",
                    options = listOf("SUV", "SEDAN", "HATCHBACK", "COUPE", "PICKUP", "VAN"),
                    onOptionSelected = { onEvent(AddVehiculoUiEvent.OnCategoriaChange(it)) },
                    optionLabel = { it },
                    modifier = Modifier.weight(1f)
                )

                KiaDropdown(
                    value = state.transmision,
                    label = "Transmisión",
                    options = listOf("Automatico", "Manual"),
                    onOptionSelected = { onEvent(AddVehiculoUiEvent.OnTransmisionChange(it)) },
                    optionLabel = { it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KiaDropdown(
                    value = state.asientos.toString(),
                    label = "Asientos",
                    options = listOf("2", "4", "5", "7", "8"),
                    onOptionSelected = { onEvent(AddVehiculoUiEvent.OnAsientosChange(it.toInt())) },
                    optionLabel = { it },
                    modifier = Modifier.weight(1f)
                )

                KiaTextField(
                    value = state.precioPorDia,
                    onValueChange = { onEvent(AddVehiculoUiEvent.OnPrecioChange(it)) },
                    label = "Precio x Día",
                    placeholder = "$ 150.00",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            KiaDateField(
                value = state.fechaIngreso,
                label = "Fecha Ingreso",
                onClick = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            KiaTextField(
                value = state.imagenUrl,
                onValueChange = { onEvent(AddVehiculoUiEvent.OnImagenUrlChange(it)) },
                label = "Imagen URL",
                placeholder = "https://ejemplo.com/imagen.jpg"
            )

            state.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            KiaPrimaryButton(
                text = "Agregar",
                onClick = { onEvent(AddVehiculoUiEvent.GuardarVehiculo) },
                enabled = state.isFormValid,
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showDatePicker) {
        KiaDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                onEvent(AddVehiculoUiEvent.OnFechaIngresoChange(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                showDatePicker = false
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AddVehiculoBodyPreview() {
    MaterialTheme {
        AddVehiculoBody(AddVehiculoUiState()) {}
    }
}
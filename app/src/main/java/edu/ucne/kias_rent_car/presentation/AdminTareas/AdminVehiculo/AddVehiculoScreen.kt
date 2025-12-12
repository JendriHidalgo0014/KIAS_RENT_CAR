package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight
import java.time.LocalDate
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehiculoBody(
    state: AddVehiculoUiState,
    onEvent: (AddVehiculoUiEvent) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

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
                navigationIcon = {
                    IconButton(onClick = { onEvent(AddVehiculoUiEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        containerColor = scrimLight
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

            Text("Modelo", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.modelo,
                onValueChange = { onEvent(AddVehiculoUiEvent.OnModeloChange(it)) },
                placeholder = { Text("Ej. Kia Rio", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Descripción", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { onEvent(AddVehiculoUiEvent.OnDescripcionChange(it)) },
                placeholder = { Text("Añade una descripción", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Categoría", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownSelector(
                        selectedValue = state.categoria,
                        options = listOf("SUV", "SEDAN", "HATCHBACK", "COUPE", "PICKUP", "VAN"),
                        onValueChange = { onEvent(AddVehiculoUiEvent.OnCategoriaChange(it)) }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Transmisión", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownSelector(
                        selectedValue = state.transmision,
                        options = listOf("Automatico", "Manual"),
                        onValueChange = { onEvent(AddVehiculoUiEvent.OnTransmisionChange(it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Asientos", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownSelector(
                        selectedValue = state.asientos.toString(),
                        options = listOf("2", "4", "5", "7", "8"),
                        onValueChange = { onEvent(AddVehiculoUiEvent.OnAsientosChange(it.toInt())) }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Precio x Día", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.precioPorDia,
                        onValueChange = { onEvent(AddVehiculoUiEvent.OnPrecioChange(it)) },
                        placeholder = { Text("$ 150.00", color = MaterialTheme.colorScheme.outline) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fecha Ingreso", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.fechaIngreso,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar fecha", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.outline)
                    }
                },
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Imagen URL", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.imagenUrl,
                onValueChange = { onEvent(AddVehiculoUiEvent.OnImagenUrlChange(it)) },
                placeholder = { Text("https://ejemplo.com/imagen.jpg", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            state.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onEvent(AddVehiculoUiEvent.GuardarVehiculo) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = state.isFormValid && !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Agregar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = LocalDate.ofEpochDay(millis / 86400000)
                        onEvent(AddVehiculoUiEvent.OnFechaIngresoChange(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    }
                    showDatePicker = false
                }) { Text("Aceptar", color = onErrorDark) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(selectedValue: String, options: List<String>, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null, tint = MaterialTheme.colorScheme.outline) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = textFieldColors(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onValueChange(option); expanded = false })
            }
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedBorderColor = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    disabledBorderColor = MaterialTheme.colorScheme.outline
)

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AddVehiculoBodyPreview() {
    MaterialTheme {
        val state = AddVehiculoUiState(modelo = "Kia Rio",
            descripcion = "Compacto económico",
            precioPorDia = "85.00")
        AddVehiculoBody(state) {}
    }
}
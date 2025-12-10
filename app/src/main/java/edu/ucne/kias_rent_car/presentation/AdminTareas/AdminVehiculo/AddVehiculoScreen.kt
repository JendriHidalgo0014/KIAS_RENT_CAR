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
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehiculoScreen(
    viewModel: AddVehiculoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KIA'S RENT CAR",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        containerColor = scrimLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Agregar Vehículo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))
            // Modelo
            Text("Modelo", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.modelo,
                onValueChange = { viewModel.onModeloChanged(it) },
                placeholder = { Text("Ej. Kia Rio", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Descripción
            Text("Descripción", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { viewModel.onDescripcionChanged(it) },
                placeholder = { Text("Añade una descripción detallada", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Categoría Dropdown
                Column(modifier = Modifier.weight(1f)) {
                    Text("Categoría", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownSelector(
                        selectedValue = state.categoria,
                        options = listOf("SUV", "SEDAN", "HATCHBACK", "COUPE", "PICKUP", "VAN"),
                        onValueChange = { viewModel.onCategoriaChanged(it) }
                    )
                }

                // Transmisión Dropdown
                Column(modifier = Modifier.weight(1f)) {
                    Text("Transmisión", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownSelector(
                        selectedValue = state.transmision,
                        options = listOf("Automatico", "Manual"),
                        onValueChange = { viewModel.onTransmisionChanged(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Asientos Dropdown
                Column(modifier = Modifier.weight(1f)) {
                    Text("Asientos", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownSelector(
                        selectedValue = state.asientos.toString(),
                        options = listOf("2", "4", "5", "7", "8"),
                        onValueChange = { viewModel.onAsientosChanged(it.toInt()) }
                    )
                }

                // Precio
                Column(modifier = Modifier.weight(1f)) {
                    Text("Precio x Día", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.precioPorDia,
                        onValueChange = { viewModel.onPrecioChanged(it) },
                        placeholder = { Text("$ 150.00", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = outlinedTextFieldColors(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha Ingreso
            Text("Fecha Ingreso", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.fechaIngreso,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Seleccionar fecha", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, null, tint = Color.Gray)
                    }
                },
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen URL
            Text("Imagen URL", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.imagenUrl,
                onValueChange = { viewModel.onImagenUrlChanged(it) },
                placeholder = { Text("https://ejemplo.com/imagen.jpg", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Error message
            state.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Agregar
            Button(
                onClick = { viewModel.guardarVehiculo() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.isFormValid && !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorDark
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Agregar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // DatePicker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = LocalDate.ofEpochDay(millis / 86400000)
                            val formatted = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            viewModel.onFechaIngresoChanged(formatted)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = onErrorDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White,
    focusedContainerColor = Color(0xFF1E1E1E),
    unfocusedContainerColor = Color(0xFF1E1E1E),
    disabledContainerColor = Color(0xFF1E1E1E),
    focusedBorderColor = Color.Gray,
    unfocusedBorderColor = Color.Gray,
    disabledBorderColor = Color.Gray
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AddVehiculoScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KIA'S RENT CAR",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        containerColor = scrimLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Agregar Vehículo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Modelo", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Kia Rio",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Descripción", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Compacto económico ideal para la ciudad",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fecha Ingreso", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "2025-01-15",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        enabled = false
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Precio x Día", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "85.00",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorDark
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Agregar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
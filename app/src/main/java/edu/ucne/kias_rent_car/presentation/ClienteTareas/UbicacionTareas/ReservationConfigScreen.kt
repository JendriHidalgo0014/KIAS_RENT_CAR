package edu.ucne.kias_rent_car.presentation.ClienteTareas.UbicacionTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
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
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationConfigScreen(
    viewModel: ReservationConfigViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onContinue: (String) -> Unit  // Navega a PaymentScreen con vehicleId
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.init(vehicleId)
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
            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "Configura tu Reserva",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Lugar de Recogida
            Text(
                text = "Lugar de Recogida",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            UbicacionDropdown(
                selectedUbicacion = state.lugarRecogida,
                ubicaciones = state.ubicaciones,
                placeholder = "Selecciona lugar de recogida",
                onUbicacionSelected = { viewModel.onEvent(ReservationConfigEvent.LugarRecogidaChanged(it)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lugar de Devolución
            Text(
                text = "Lugar de Devolución",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            UbicacionDropdown(
                selectedUbicacion = state.lugarDevolucion,
                ubicaciones = state.ubicaciones,
                placeholder = "Selecciona lugar de devolución",
                onUbicacionSelected = { viewModel.onEvent(ReservationConfigEvent.LugarDevolucionChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fecha y Hora de Recogida
            DateTimeSelector(
                title = "Fecha y Hora de Recogida",
                subtitle = state.fechaRecogida?.let { fecha ->
                    state.horaRecogida?.let { hora ->
                        "${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${hora.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    }
                } ?: "Elige fecha y hora",
                onDateSelected = { viewModel.onEvent(ReservationConfigEvent.FechaRecogidaChanged(it)) },
                onTimeSelected = { viewModel.onEvent(ReservationConfigEvent.HoraRecogidaChanged(it)) },
                selectedDate = state.fechaRecogida,
                selectedTime = state.horaRecogida
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha y Hora de Devolución
            DateTimeSelector(
                title = "Fecha y Hora de Devolución",
                subtitle = state.fechaDevolucion?.let { fecha ->
                    state.horaDevolucion?.let { hora ->
                        "${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${hora.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    }
                } ?: "Elige fecha y hora",
                onDateSelected = { viewModel.onEvent(ReservationConfigEvent.FechaDevolucionChanged(it)) },
                onTimeSelected = { viewModel.onEvent(ReservationConfigEvent.HoraDevolucionChanged(it)) },
                selectedDate = state.fechaDevolucion,
                selectedTime = state.horaDevolucion,
                minDate = state.fechaRecogida
            )

            Spacer(modifier = Modifier.weight(1f))

            // Error
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Botón Continuar
            Button(
                onClick = {
                    if (viewModel.validateForm()) {
                        viewModel.saveConfig()
                        onContinue(vehicleId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorDark,
                    contentColor = Color.White,
                    disabledContainerColor = onErrorDark.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UbicacionDropdown(
    selectedUbicacion: Ubicacion?,
    ubicaciones: List<Ubicacion>,
    placeholder: String,
    onUbicacionSelected: (Ubicacion) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedUbicacion?.nombre ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Row {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = onErrorDark
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1E1E1E)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF2D2D2D))
        ) {
            ubicaciones.forEach { ubicacion ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = ubicacion.nombre,
                            color = Color.White
                        )
                    },
                    onClick = {
                        onUbicacionSelected(ubicacion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelector(
    title: String,
    subtitle: String,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    selectedDate: LocalDate?,
    selectedTime: LocalTime?,
    minDate: LocalDate? = null
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.toEpochDay()?.times(86400000)
                ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = LocalDate.ofEpochDay(millis / 86400000)
                            onDateSelected(date)
                        }
                        showDatePicker = false
                        showTimePicker = true
                    }
                ) {
                    Text("Siguiente", color = onErrorDark)
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

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedTime?.hour ?: 10,
            initialMinute = selectedTime?.minute ?: 0
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
                        showTimePicker = false
                    }
                ) {
                    Text("Aceptar", color = onErrorDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            title = {
                Text("Selecciona la hora", color = Color.White)
            },
            text = {
                TimePicker(state = timePickerState)
            },
            containerColor = Color(0xFF2D2D2D)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ReservationConfigScreenPreview() {
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
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Configura tu Reserva",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Lugar de Recogida", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Aeropuerto SDQ",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.LocationOn, null, tint = onErrorDark)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Lugar de Devolución", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Aeropuerto SDQ",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.LocationOn, null, tint = onErrorDark)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Fecha y Hora de Recogida", fontSize = 16.sp, color = Color.White)
                        Text(text = "15/01/2025 - 10:00", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = "Continuar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
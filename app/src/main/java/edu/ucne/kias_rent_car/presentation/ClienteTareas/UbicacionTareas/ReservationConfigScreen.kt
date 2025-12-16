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

@Composable
fun ReservationConfigScreen(
    viewModel: ReservationConfigViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onContinue: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.onEvent(ReservationConfigUiEvent.Init(vehicleId))
    }

    LaunchedEffect(state.configSaved) {
        if (state.configSaved) onContinue(vehicleId)
    }

    ReservationConfigBody(
        state = state,
        onEvent = { event ->
            when (event) {
                ReservationConfigUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationConfigBody(
    state: ReservationConfigUiState,
    onEvent: (ReservationConfigUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ReservationConfigUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
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
            Spacer(modifier = Modifier.height(24.dp))

            Text("Configura tu Reserva", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Lugar de Recogida", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            UbicacionDropdown(
                selectedUbicacion = state.lugarRecogida,
                ubicaciones = state.ubicaciones,
                placeholder = "Selecciona lugar de recogida",
                onUbicacionSelected = { onEvent(ReservationConfigUiEvent.OnLugarRecogidaChange(it)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Lugar de Devolución", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            UbicacionDropdown(
                selectedUbicacion = state.lugarDevolucion,
                ubicaciones = state.ubicaciones,
                placeholder = "Selecciona lugar de devolución",
                onUbicacionSelected = { onEvent(ReservationConfigUiEvent.OnLugarDevolucionChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            DateTimeSelector(
                title = "Fecha y Hora de Recogida",
                subtitle = state.fechaRecogida?.let { fecha ->
                    state.horaRecogida?.let { hora ->
                        "${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${hora.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    }
                } ?: "Elige fecha y hora",
                onDateSelected = { onEvent(ReservationConfigUiEvent.OnFechaRecogidaChange(it)) },
                onTimeSelected = { onEvent(ReservationConfigUiEvent.OnHoraRecogidaChange(it)) },
                selectedDate = state.fechaRecogida,
                selectedTime = state.horaRecogida
            )

            Spacer(modifier = Modifier.height(16.dp))

            DateTimeSelector(
                title = "Fecha y Hora de Devolución",
                subtitle = state.fechaDevolucion?.let { fecha ->
                    state.horaDevolucion?.let { hora ->
                        "${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${hora.format(DateTimeFormatter.ofPattern("HH:mm"))}"
                    }
                } ?: "Elige fecha y hora",
                onDateSelected = { onEvent(ReservationConfigUiEvent.OnFechaDevolucionChange(it)) },
                onTimeSelected = { onEvent(ReservationConfigUiEvent.OnHoraDevolucionChange(it)) },
                selectedDate = state.fechaDevolucion,
                selectedTime = state.horaDevolucion
            )

            Spacer(modifier = Modifier.weight(1f))

            state.error?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            Button(
                onClick = { onEvent(ReservationConfigUiEvent.Continuar) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = state.isFormValid,
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark, disabledContainerColor = onErrorDark.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Continuar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedUbicacion?.nombre ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.outline) },
            trailingIcon = {
                Row {
                    Icon(Icons.Default.LocationOn, null, tint = onErrorDark)
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = MaterialTheme.colorScheme.outline)
                }
            },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            ubicaciones.forEach { ubicacion ->
                DropdownMenuItem(
                    text = { Text(ubicacion.nombre, color = MaterialTheme.colorScheme.onSurface) },
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
    selectedTime: LocalTime?
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.outline)
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.toEpochDay()?.times(86400000) ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(LocalDate.ofEpochDay(millis / 86400000))
                    }
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Siguiente", color = onErrorDark) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar", color = MaterialTheme.colorScheme.outline) }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = selectedTime?.hour ?: 10, initialMinute = selectedTime?.minute ?: 0)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
                    showTimePicker = false
                }) { Text("Aceptar", color = onErrorDark) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar", color = MaterialTheme.colorScheme.outline) }
            },
            title = { Text("Selecciona la hora", color = MaterialTheme.colorScheme.onSurface) },
            text = { TimePicker(state = timePickerState) },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ReservationConfigBodyPreview() {
    val state = ReservationConfigUiState()
    ReservationConfigBody(state) {}
}
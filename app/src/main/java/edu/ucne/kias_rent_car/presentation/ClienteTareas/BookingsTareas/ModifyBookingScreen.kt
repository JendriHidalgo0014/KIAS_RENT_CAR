package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ModifyBookingScreen(
    viewModel: ModifyBookingViewModel = hiltViewModel(),
    bookingId: Int,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId) { viewModel.onEvent(ModifyBookingUiEvent.LoadBooking(bookingId)) }
    LaunchedEffect(state.saveSuccess) { if (state.saveSuccess) onSaveSuccess() }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyBookingBody(
    state: ModifyBookingUiState,
    onEvent: (ModifyBookingUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ModifyBookingUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Modificar Reserva", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Lugar de Recogida", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            UbicacionDropdown(
                selectedUbicacion = state.ubicacionRecogida,
                ubicaciones = state.ubicaciones,
                placeholder = "Selecciona lugar de recogida",
                onUbicacionSelected = { onEvent(ModifyBookingUiEvent.OnUbicacionRecogidaChange(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lugar de Devolución", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            UbicacionDropdown(
                selectedUbicacion = state.ubicacionDevolucion,
                ubicaciones = state.ubicaciones,
                placeholder = "Selecciona lugar de devolución",
                onUbicacionSelected = { onEvent(ModifyBookingUiEvent.OnUbicacionDevolucionChange(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fecha de Recogida", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            DateTimeField(value = state.fechaRecogida) { fecha, hora ->
                onEvent(ModifyBookingUiEvent.OnFechaRecogidaChange(fecha, hora))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fecha de Devolución", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            DateTimeField(value = state.fechaDevolucion) { fecha, hora ->
                onEvent(ModifyBookingUiEvent.OnFechaDevolucionChange(fecha, hora))
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(ModifyBookingUiEvent.GuardarCambios) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Guardar Cambios", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
            colors = textFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            ubicaciones.forEach { ubicacion ->
                DropdownMenuItem(
                    text = { Text(ubicacion.nombre, color = MaterialTheme.colorScheme.onSurface) },
                    onClick = { onUbicacionSelected(ubicacion); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeField(value: String, onDateTimeSelected: (LocalDate, LocalTime) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
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

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate = LocalDate.ofEpochDay(it / 86400000) }
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Siguiente", color = onErrorDark) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = 10, initialMinute = 0)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate?.let { onDateTimeSelected(it, LocalTime.of(timePickerState.hour, timePickerState.minute)) }
                    showTimePicker = false
                }) { Text("Aceptar", color = onErrorDark) }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") } },
            title = { Text("Selecciona la hora", color = MaterialTheme.colorScheme.onSurface) },
            text = { TimePicker(state = timePickerState) },
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
private fun ModifyBookingBodyPreview() {
    MaterialTheme {
        val state = ModifyBookingUiState(
            fechaRecogida = "2025-01-15 - 10:00",
            fechaDevolucion = "2025-01-20 - 10:00"
        )
        ModifyBookingBody(state) {}
    }
}
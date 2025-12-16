package edu.ucne.kias_rent_car.presentation.Components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiaTimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    initialHour: Int = 10,
    initialMinute: Int = 0
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
            }) {
                Text("Aceptar", color = onErrorDark)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.outline)
            }
        },
        title = { Text("Selecciona la hora", color = MaterialTheme.colorScheme.onSurface) },
        text = { TimePicker(state = timePickerState) },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
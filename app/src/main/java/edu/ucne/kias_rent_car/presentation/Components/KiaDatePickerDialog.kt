package edu.ucne.kias_rent_car.presentation.Components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiaDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    initialDateMillis: Long = System.currentTimeMillis()
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(LocalDate.ofEpochDay(millis / 86400000))
                }
            }) { Text("Aceptar", color = onErrorDark) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
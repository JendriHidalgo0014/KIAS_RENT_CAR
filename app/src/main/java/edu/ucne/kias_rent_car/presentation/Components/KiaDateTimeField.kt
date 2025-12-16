package edu.ucne.kias_rent_car.presentation.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiaDateTimeField(
    label: String,
    value: String,
    onDateTimeSelected: (LocalDate, LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = modifier) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.outline)
                }
            },
            colors = kiaTextFieldColors(),
            shape = RoundedCornerShape(12.dp),
            enabled = false
        )
    }

    if (showDatePicker) {
        KiaDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
                showTimePicker = true
            }
        )
    }

    if (showTimePicker) {
        KiaTimePickerDialog(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { time ->
                selectedDate?.let { date ->
                    onDateTimeSelected(date, time)
                }
                showTimePicker = false
            }
        )
    }
}
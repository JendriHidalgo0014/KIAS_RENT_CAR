package edu.ucne.kias_rent_car.presentation.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.ui.theme.onErrorDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiaUbicacionDropdown(
    label: String,
    selectedUbicacion: Ubicacion?,
    ubicaciones: List<Ubicacion>,
    placeholder: String,
    onUbicacionSelected: (Ubicacion) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = kiaTextFieldColors(),
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
                        onClick = {
                            onUbicacionSelected(ubicacion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
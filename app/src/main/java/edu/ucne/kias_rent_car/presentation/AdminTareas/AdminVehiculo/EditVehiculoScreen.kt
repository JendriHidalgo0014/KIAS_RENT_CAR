package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun EditVehiculoScreen(
    vehiculoId: String,
    viewModel: EditVehiculoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    onDelete: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehiculoId) {
        viewModel.onEvent(EditVehiculoUiEvent.LoadVehiculo(vehiculoId))
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onSaveSuccess()
    }
    LaunchedEffect(state.deleteSuccess) {
        if (state.deleteSuccess) onDelete()
    }

    EditVehiculoBody(
        state = state,
        onEvent = { event ->
            when (event) {
                EditVehiculoUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVehiculoBody(
    state: EditVehiculoUiState,
    onEvent: (EditVehiculoUiEvent) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(EditVehiculoUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Eliminar",
                            tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        containerColor = scrimLight
    ) { padding ->
        if (state.isLoading && state.modelo.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = onErrorDark)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Editar Vehículo", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))

                if (state.imagenUrl.isNotBlank()) {
                    AsyncImage(
                        model = state.imagenUrl, contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Modelo", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.modelo,
                    onValueChange = { onEvent(EditVehiculoUiEvent.OnModeloChange(it)) },
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
                    onValueChange = { onEvent(EditVehiculoUiEvent.OnDescripcionChange(it)) },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Categoría", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownSelector(state.categoria,
                            listOf("SUV", "SEDAN", "HATCHBACK", "COUPE", "ELECTRICO", "HIBRIDO")) {
                            onEvent(EditVehiculoUiEvent.OnCategoriaChange(it))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Transmisión", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownSelector(state.transmision,
                            listOf("Automatico", "Manual")) {
                            onEvent(EditVehiculoUiEvent.OnTransmisionChange(it))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Asientos", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownSelector(state.asientos.toString(), listOf("2", "4", "5", "7", "8")) {
                            onEvent(EditVehiculoUiEvent.OnAsientosChange(it.toInt()))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Precio x Día", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.precioPorDia,
                            onValueChange = { onEvent(EditVehiculoUiEvent.OnPrecioChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Imagen URL", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.imagenUrl,
                    onValueChange = { onEvent(EditVehiculoUiEvent.OnImagenUrlChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                state.error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { onEvent(EditVehiculoUiEvent.GuardarCambios) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = state.isFormValid && !state.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary)
                    else Text("Guardar Cambios", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Vehículo") },
            text = { Text("¿Estás seguro de eliminar este vehículo?") },
            confirmButton = {
                TextButton(onClick = { onEvent(EditVehiculoUiEvent.EliminarVehiculo); showDeleteDialog = false }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(selectedValue: String, options: List<String>, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedValue, onValueChange = {}, readOnly = true,
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null,
                tint = MaterialTheme.colorScheme.outline) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = textFieldColors(), shape = RoundedCornerShape(12.dp), singleLine = true
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { DropdownMenuItem(text = { Text(it) },
                onClick = { onValueChange(it); expanded = false }) }
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
    focusedBorderColor = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline
)

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun EditVehiculoBodyPreview() {
    val state = EditVehiculoUiState(modelo = "Kia Sportage",
        descripcion = "SUV compacto",
        precioPorDia = "120.00",
        isFormValid = true)
    EditVehiculoBody(state) {}
}
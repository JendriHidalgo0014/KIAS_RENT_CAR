package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyEstadoReservaScreen(
    viewModel: ModifyEstadoReservaViewModel = hiltViewModel(),
    reservacionId: Int,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(reservacionId) {
        viewModel.loadReservacion(reservacionId)
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            onSaveSuccess()
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
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Modificar Estado de\nReserva",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
            // Info de la reserva
            InfoRow("ID de Reserva", state.codigoReserva)
            InfoRow("Nombre del Cliente", state.nombreCliente)
            InfoRow("Vehículo", state.vehiculo)
            InfoRow("Periodo de Alquiler", state.periodo)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Seleccionar Nuevo Estado",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Opciones de estado
            val estados = listOf("Confirmada", "Cancelada", "En Proceso", "Finalizada")
            estados.forEach { estado ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.estadoSeleccionado == estado,
                        onClick = { viewModel.onEstadoChanged(estado) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = onErrorDark,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = estado,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            // Botón Guardar
            Button(
                onClick = { viewModel.guardarCambios() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading,
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
                        text = "Guardar Cambios",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 14.sp, color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ModifyEstadoReservaScreenPreview() {
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Modificar Estado de\nReserva",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoRow("ID de Reserva", "KR-123456")
            InfoRow("Nombre del Cliente", "Juan Pérez")
            InfoRow("Vehículo", "Kia Sportage")
            InfoRow("Periodo de Alquiler", "15/01 - 20/01/2025")

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Seleccionar Nuevo Estado",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            val estados = listOf("Confirmado", "Cancelado", "En Proceso", "Completado")
            estados.forEach { estado ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = estado == "Confirmado",
                        onClick = {},
                        colors = RadioButtonDefaults.colors(
                            selectedColor = onErrorDark,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = estado,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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
                    text = "Guardar Cambios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
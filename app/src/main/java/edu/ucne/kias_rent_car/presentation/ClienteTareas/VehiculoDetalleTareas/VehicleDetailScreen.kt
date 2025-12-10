package edu.ucne.kias_rent_car.presentation.ClienteTareas.VehiculoDetalleTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    viewModel: VehicleDetailViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onNavigateToReservation: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.loadVehicle(vehicleId)
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
        // En el Scaffold, cambia el bottomBar:
        bottomBar = {
            state.vehicle?.let { vehicle ->
                VehicleDetailBottomBar(
                    disponible = vehicle.disponible,
                    onReservarClick = {
                        onNavigateToReservation(vehicle.id)
                    }
                )
            }
        },
        containerColor = scrimLight

    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = onErrorDark)
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error ?: "Error desconocido",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateBack,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onErrorDark
                            )
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
            state.vehicle != null -> {
                VehicleDetailContent(
                    vehicle = state.vehicle!!,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun VehicleDetailContent(
    vehicle: Vehicle,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen del vehículo con gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(vehicle.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = vehicle.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2D2D2D)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = onErrorDark)
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2D2D2D)),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                }
            )

            // Gradiente inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                scrimLight
                            )
                        )
                    )
            )
        }

        // Información del vehículo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Modelo
            Text(
                text = vehicle.modelo,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = vehicle.descripcion.ifEmpty {
                    "Motor V4, ${vehicle.asientos} pasajeros, excelente comodidad en la carretera y un consumo económico."
                },
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Cards de información - Primera fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    title = "Categoría",
                    value = vehicle.categoria.displayName,
                    modifier = Modifier.weight(1f)
                )

                InfoCard(
                    title = "Precio x Día",
                    value = "RD$${vehicle.precioPorDia.toInt()}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cards de información - Segunda fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    title = "Transmisión",
                    value = vehicle.transmision.displayName,
                    modifier = Modifier.weight(1f)
                )

                InfoCard(
                    title = "Capacidad",
                    value = "${vehicle.asientos} pasajeros",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Estado de disponibilidad
            InfoCard(
                title = "Estado",
                value = if (vehicle.disponible) "Disponible" else "No disponible",
                modifier = Modifier.fillMaxWidth(),
                valueColor = if (vehicle.disponible) Color(0xFF4CAF50) else onErrorDark
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.White
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = valueColor
            )
        }
    }
}

@Composable
private fun VehicleDetailBottomBar(
    disponible: Boolean,
    onReservarClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = scrimLight,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = onReservarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = disponible,
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorDark,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = if (disponible) "Reservar" else "No Disponible",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun VehicleDetailScreenPreview() {
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
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scrimLight
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(text = "Reservar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = scrimLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color(0xFF2D2D2D)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(80.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Kia Sportage",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Motor V4, 5 pasajeros, excelente comodidad.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(title = "Categoría", value = "SUV", modifier = Modifier.weight(1f))
                    InfoCard(title = "Precio x Día", value = "RD$120", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(title = "Transmisión", value = "Automático", modifier = Modifier.weight(1f))
                    InfoCard(title = "Capacidad", value = "5 pasajeros", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                InfoCard(
                    title = "Estado",
                    value = "Disponible",
                    modifier = Modifier.fillMaxWidth(),
                    valueColor = Color(0xFF4CAF50)
                )
            }
        }
    }
}
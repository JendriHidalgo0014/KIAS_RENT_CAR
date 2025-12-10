package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import edu.ucne.kias_rent_car.presentation.Components.KiaBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    viewModel: BookingDetailViewModel = hiltViewModel(),
    bookingId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToModify: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
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
        bottomBar = {
            KiaBottomNavigation(
                currentRoute = "bookings",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "bookings" -> onNavigateToBookings()
                        "support" -> onNavigateToSupport()
                        "profile" -> onNavigateToProfile()
                    }
                }
            )
        },
        containerColor = scrimLight
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = onErrorDark)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen del vehículo
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.reservacion?.vehiculo?.imagenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
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
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Nombre del vehículo
                    Text(
                        text = state.reservacion?.vehiculo?.modelo ?: "",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.reservacion?.vehiculo?.categoria?.displayName ?: "",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "•",
                            color = Color.Gray
                        )
                        Text(
                            text = "${state.reservacion?.vehiculo?.asientos ?: 0} Asientos",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "•",
                            color = Color.Gray
                        )
                        Text(
                            text = state.reservacion?.vehiculo?.transmision?.displayName ?: "",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado
                    val estadoColor = when (state.reservacion?.estado) {
                        "Confirmada" -> Color(0xFF4CAF50)
                        "Pendiente" -> Color(0xFFFF9800)
                        "Cancelada" -> onErrorDark
                        else -> Color.Gray
                    }

                    Text(
                        text = state.reservacion?.estado ?: "",
                        fontSize = 14.sp,
                        color = estadoColor,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Detalles
                    DetailRow(
                        icon = Icons.Default.CalendarMonth,
                        label = "Recogida",
                        value = "${state.reservacion?.fechaRecogida} - ${state.reservacion?.horaRecogida}"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Default.CalendarMonth,
                        label = "Devolución",
                        value = "${state.reservacion?.fechaDevolucion} - ${state.reservacion?.horaDevolucion}"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Default.LocationOn,
                        label = "Lugar de Recogida",
                        value = state.reservacion?.ubicacionRecogida?.nombre ?: ""
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(
                        icon = Icons.Default.LocationOn,
                        label = "Lugar de Devolución",
                        value = state.reservacion?.ubicacionDevolucion?.nombre ?: ""
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Price Breakdown
                    Text(
                        text = "Price Breakdown",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PriceDetailRow("Subtotal", "$${String.format("%.2f", state.reservacion?.subtotal ?: 0.0)}")
                    PriceDetailRow("Impuestos", "$${String.format("%.2f", state.reservacion?.impuestos ?: 0.0)}")

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$${String.format("%.2f", state.reservacion?.total ?: 0.0)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = onErrorDark
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botones
                    if (state.reservacion?.estado in listOf("Confirmada", "Pendiente")) {
                        Button(
                            onClick = { onNavigateToModify(bookingId) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onErrorDark
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text(
                                text = "Modificar Reserva",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { viewModel.cancelarReserva() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Cancelar Reserva",
                                color = onErrorDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = onErrorDark,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PriceDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 14.sp, color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BookingDetailScreenPreview() {
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
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF2D2D2D)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(60.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Kia Sportage",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "SUV", fontSize = 14.sp, color = Color.Gray)
                    Text(text = "•", color = Color.Gray)
                    Text(text = "5 Asientos", fontSize = 14.sp, color = Color.Gray)
                    Text(text = "•", color = Color.Gray)
                    Text(text = "Automático", fontSize = 14.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirmada",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                DetailRow(Icons.Default.CalendarMonth, "Recogida", "2025-01-15 - 10:00")
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow(Icons.Default.CalendarMonth, "Devolución", "2025-01-20 - 10:00")
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow(Icons.Default.LocationOn, "Lugar de Recogida", "Aeropuerto SDQ")
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow(Icons.Default.LocationOn, "Lugar de Devolución", "Aeropuerto SDQ")

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Price Breakdown",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                PriceDetailRow("Subtotal", "$500.00")
                PriceDetailRow("Impuestos", "$90.00")

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "$590.00",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = onErrorDark
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = "Modificar Reserva", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
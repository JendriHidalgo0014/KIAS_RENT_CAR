package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.presentation.Components.KiaBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    viewModel: BookingsViewModel = hiltViewModel(),
    onNavigateToBookingDetail: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                        "bookings" -> { }
                        "support" -> onNavigateToSupport()
                        "profile" -> onNavigateToProfile()
                    }
                }
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
                text = "Reservas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.filtro == BookingFilter.ACTUALES,
                    onClick = { viewModel.onEvent(BookingsEvent.FilterChanged(BookingFilter.ACTUALES)) },
                    label = { Text("Actuales") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = onErrorDark,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF2D2D2D),
                        labelColor = Color.White
                    )
                )
                FilterChip(
                    selected = state.filtro == BookingFilter.PASADAS,
                    onClick = { viewModel.onEvent(BookingsEvent.FilterChanged(BookingFilter.PASADAS)) },
                    label = { Text("Pasadas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = onErrorDark,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF2D2D2D),
                        labelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = onErrorDark)
                }
            } else if (state.reservaciones.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes reservaciones",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.reservaciones) { reservacion ->
                        BookingCard(
                            reservacion = reservacion,
                            onClick = { onNavigateToBookingDetail(reservacion.reservacionId) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingCard(
    reservacion: Reservacion,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Imagen
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(reservacion.vehiculo?.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = reservacion.vehiculo?.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2D2D2D)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = onErrorDark,
                            modifier = Modifier.size(20.dp)
                        )
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
                            tint = Color.Gray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Estado
                val estadoColor = when (reservacion.estado) {
                    "Confirmada" -> Color.Green
                    "Pendiente" -> Color.Yellow
                    "Cancelada" -> onErrorDark
                    "Finalizada" -> Color.Gray
                    else -> Color.Gray
                }

                Text(
                    text = reservacion.estado,
                    fontSize = 12.sp,
                    color = estadoColor,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reservacion.vehiculo?.modelo ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${reservacion.fechaRecogida} - ${reservacion.fechaDevolucion}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = onErrorDark
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (reservacion.estado == "Pendiente") "Gestionar Reserva" else "Ver Detalles",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun BookingsScreenPreview() {
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
                text = "Reservas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text("Actuales") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = onErrorDark,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = false,
                    onClick = {},
                    label = { Text("Pasadas") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF2D2D2D),
                        labelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card de ejemplo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2D2D2D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Confirmada",
                            fontSize = 12.sp,
                            color = Color.Green,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kia Sportage",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "15/01/2025 - 20/01/2025",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Ver Detalles", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
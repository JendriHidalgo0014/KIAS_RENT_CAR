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
import androidx.compose.runtime.*
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

@Composable
fun VehicleDetailScreen(
    viewModel: VehicleDetailViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onNavigateToReservation: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.onEvent(VehicleDetailUiEvent.LoadVehicle(vehicleId))
    }

    VehicleDetailBody(
        state = state,
        onEvent = { event ->
            when (event) {
                VehicleDetailUiEvent.NavigateBack -> onNavigateBack()
                is VehicleDetailUiEvent.NavigateToReservation -> onNavigateToReservation(event.vehicleId)
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailBody(
    state: VehicleDetailUiState,
    onEvent: (VehicleDetailUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(VehicleDetailUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        bottomBar = {
            state.vehicle?.let { vehicle ->
                VehicleDetailBottomBar(
                    disponible = vehicle.disponible,
                    onReservarClick = { onEvent(VehicleDetailUiEvent.NavigateToReservation(vehicle.id)) }
                )
            }
        },
        containerColor = scrimLight
    ) { padding ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = onErrorDark)
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.error, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onEvent(VehicleDetailUiEvent.NavigateBack) },
                            colors = ButtonDefaults.buttonColors(containerColor = onErrorDark)
                        ) { Text("Volver") }
                    }
                }
            }
            state.vehicle != null -> {
                VehicleDetailContent(vehicle = state.vehicle, paddingValues = padding)
            }
        }
    }
}

@Composable
private fun VehicleDetailContent(vehicle: Vehicle, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(vehicle.imagenUrl).crossfade(true).build(),
                contentDescription = vehicle.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = onErrorDark)
                    }
                },
                error = {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) { }
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(colors = listOf(Color.Transparent, scrimLight)))
            )
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
            Text(vehicle.modelo, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = vehicle.descripcion.ifEmpty { "Motor V4, ${vehicle.asientos} pasajeros, excelente comodidad en la carretera y un consumo económico." },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoCard("Categoría", vehicle.categoria.displayName, Modifier.weight(1f))
                InfoCard("Precio x Día", "RD$${vehicle.precioPorDia.toInt()}", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoCard("Transmisión", vehicle.transmision.displayName, Modifier.weight(1f))
                InfoCard("Capacidad", "${vehicle.asientos} pasajeros", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

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
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
        }
    }
}

@Composable
private fun VehicleDetailBottomBar(disponible: Boolean, onReservarClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = scrimLight, shadowElevation = 8.dp) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
            Button(
                onClick = onReservarClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = disponible,
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorDark,
                    disabledContainerColor = MaterialTheme.colorScheme.outline,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(if (disponible) "Reservar" else "No Disponible", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun VehicleDetailBodyPreview() {
    val state = VehicleDetailUiState()
    VehicleDetailBody(state) {}
}
package edu.ucne.kias_rent_car.presentation.ReservationTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import edu.ucne.kias_rent_car.presentation.Components.KiaLoadingBox
import edu.ucne.kias_rent_car.presentation.Components.KiaPrimaryButton
import edu.ucne.kias_rent_car.presentation.Components.KiaScaffold
import edu.ucne.kias_rent_car.ui.theme.onErrorDark

@Composable
fun ReservationConfirmationScreen(
    viewModel: ReservationConfirmationViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.onEvent(ReservationConfirmationUiEvent.LoadData(vehicleId))
    }

    ReservationConfirmationBody(
        state = state,
        vehicleId = vehicleId,
        onEvent = { event ->
            when (event) {
                ReservationConfirmationUiEvent.NavigateBack -> onNavigateBack()
                is ReservationConfirmationUiEvent.ConfirmarReserva -> onConfirm(event.vehicleId)
                ReservationConfirmationUiEvent.Cancelar -> onCancel()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ReservationConfirmationBody(
    state: ReservationConfirmationUiState,
    vehicleId: String,
    onEvent: (ReservationConfirmationUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(ReservationConfirmationUiEvent.NavigateBack) }
    ) { padding ->
        if (state.isLoading) {
            KiaLoadingBox(padding)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirmación de Reserva",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                VehicleImageCard(
                    imageUrl = state.vehicle?.imagenUrl,
                    modelo = state.vehicle?.modelo
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    state.vehicle?.modelo ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    state.vehicle?.categoria?.displayName ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(24.dp))

                ReservationDetailsSection(state)

                Spacer(modifier = Modifier.height(24.dp))

                PriceSummarySection(state)

                Spacer(modifier = Modifier.height(32.dp))

                KiaPrimaryButton(
                    text = "Confirmar Reserva",
                    onClick = { onEvent(ReservationConfirmationUiEvent.ConfirmarReserva(vehicleId)) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { onEvent(ReservationConfirmationUiEvent.Cancelar) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", fontSize = 16.sp, color = MaterialTheme.colorScheme.outline)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun VehicleImageCard(imageUrl: String?, modelo: String?) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = modelo,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp)),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = onErrorDark)
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    )
}

@Composable
private fun ReservationDetailsSection(state: ReservationConfirmationUiState) {
    DetailItem(Icons.Default.CalendarMonth, "Recogida", state.fechaRecogida)
    Spacer(modifier = Modifier.height(12.dp))
    DetailItem(Icons.Default.CalendarMonth, "Devolución", state.fechaDevolucion)
    Spacer(modifier = Modifier.height(12.dp))
    DetailItem(Icons.Default.LocationOn, "Lugar de Recogida", state.lugarRecogida)
    Spacer(modifier = Modifier.height(12.dp))
    DetailItem(Icons.Default.LocationOn, "Lugar de Devolución", state.lugarDevolucion)
}

@Composable
private fun PriceSummarySection(state: ReservationConfirmationUiState) {
    Text(
        "Resumen de Precio",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    PriceRow("Subtotal (${state.dias} días)", "$${String.format("%.2f", state.subtotal)}")
    PriceRow("Impuestos y tasas", "$${String.format("%.2f", state.impuestos)}")

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Total",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            "$${String.format("%.2f", state.total)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = onErrorDark
        )
    }
}

@Composable
private fun DetailItem(icon: ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = onErrorDark, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            Text(
                value,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
        Text(value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ReservationConfirmationBodyPreview() {
    MaterialTheme {
        ReservationConfirmationBody(
            state = ReservationConfirmationUiState(
                fechaRecogida = "15/01/2025 - 10:00",
                fechaDevolucion = "20/01/2025 - 10:00",
                lugarRecogida = "Aeropuerto SDQ",
                lugarDevolucion = "Aeropuerto SDQ",
                dias = 5,
                subtotal = 500.0,
                impuestos = 90.0,
                total = 590.0
            ),
            vehicleId = "1"
        ) {}
    }
}
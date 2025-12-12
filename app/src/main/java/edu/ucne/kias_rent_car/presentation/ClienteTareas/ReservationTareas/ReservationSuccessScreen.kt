package edu.ucne.kias_rent_car.presentation.ReservationTareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun ReservationSuccessScreen(
    viewModel: ReservationSuccessViewModel = hiltViewModel(),
    reservacionId: String,
    onVerDetalles: (String) -> Unit,
    onVolverInicio: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(reservacionId) {
        viewModel.onEvent(ReservationSuccessUiEvent.LoadReservacion(reservacionId))
    }

    ReservationSuccessBody(
        state = state,
        reservacionId = reservacionId,
        onEvent = { event ->
            when (event) {
                is ReservationSuccessUiEvent.VerDetalles -> onVerDetalles(event.reservacionId)
                ReservationSuccessUiEvent.VolverInicio -> onVolverInicio()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationSuccessBody(
    state: ReservationSuccessUiState,
    reservacionId: String,
    onEvent: (ReservationSuccessUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(100.dp).background(onErrorDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, "Éxito", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¡Reserva\nConfirmada!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Felicitaciones! Tu reserva se ha completado con éxito.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Código de Reserva", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    Text(state.codigoReserva, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { onEvent(ReservationSuccessUiEvent.VerDetalles(reservacionId)) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Ver Detalles de la Reserva", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { onEvent(ReservationSuccessUiEvent.VolverInicio) }) {
                Text("Volver al Inicio", fontSize = 16.sp, color = onErrorDark)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ReservationSuccessBodyPreview() {
    MaterialTheme {
        val state = ReservationSuccessUiState(codigoReserva = "KR-123456")
        ReservationSuccessBody(state, "1") {}
    }
}
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.presentation.Components.KiaPrimaryButton
import edu.ucne.kias_rent_car.presentation.Components.KiaScaffold
import edu.ucne.kias_rent_car.presentation.Components.kiaTextFieldColors

@Composable
fun ResponderMensajeScreen(
    viewModel: ResponderMensajeViewModel = hiltViewModel(),
    mensajeId: String,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(mensajeId) {
        viewModel.onEvent(ResponderMensajeUiEvent.LoadMensaje(mensajeId))
    }

    LaunchedEffect(state.enviado) {
        if (state.enviado) onSuccess()
    }

    ResponderMensajeBody(
        state = state,
        onEvent = { event ->
            when (event) {
                ResponderMensajeUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ResponderMensajeBody(
    state: ResponderMensajeUiState,
    onEvent: (ResponderMensajeUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(ResponderMensajeUiEvent.NavigateBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contestar Mensaje",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            MensajeOriginalCard(
                nombreUsuario = state.nombreUsuario,
                mensaje = state.mensajeOriginal
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Tu Respuesta", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.respuesta,
                onValueChange = { onEvent(ResponderMensajeUiEvent.OnRespuestaChange(it)) },
                placeholder = { Text("Escribe tu respuesta aquí...", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = kiaTextFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            KiaPrimaryButton(
                text = "Enviar Respuesta",
                onClick = { onEvent(ResponderMensajeUiEvent.EnviarRespuesta) },
                enabled = state.respuesta.isNotBlank(),
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MensajeOriginalCard(
    nombreUsuario: String,
    mensaje: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombreUsuario.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = nombreUsuario,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = mensaje,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ResponderMensajeBodyPreview() {
    MaterialTheme {
        ResponderMensajeBody(
            ResponderMensajeUiState(
                nombreUsuario = "Juan Pérez",
                mensajeOriginal = "Hola, quisiera saber si puedo modificar mi reserva."
            )
        ) {}
    }
}
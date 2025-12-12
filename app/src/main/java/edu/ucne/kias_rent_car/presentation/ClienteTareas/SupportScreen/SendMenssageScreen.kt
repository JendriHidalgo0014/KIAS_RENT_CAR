package edu.ucne.kias_rent_car.presentation.SupportTareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun SendMessageScreen(
    viewModel: SendMessageViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onMessageSent: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.messageSent) {
        if (state.messageSent) onMessageSent()
    }

    SendMessageBody(
        state = state,
        onEvent = { event ->
            when (event) {
                SendMessageUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageBody(
    state: SendMessageUiState,
    onEvent: (SendMessageUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enviar Mensaje", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SendMessageUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Asunto", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.asunto,
                onValueChange = { onEvent(SendMessageUiEvent.OnAsuntoChange(it)) },
                placeholder = { Text("Escribe el asunto", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Mensaje", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.mensaje,
                onValueChange = { onEvent(SendMessageUiEvent.OnMensajeChange(it)) },
                placeholder = { Text("Escribe tu mensaje aquí...", color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(SendMessageUiEvent.EnviarMensaje) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = state.asunto.isNotBlank() && state.mensaje.isNotBlank() && !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Enviar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedBorderColor = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline
)

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SendMessageBodyPreview() {
    MaterialTheme {
        val state = SendMessageUiState(asunto = "Consulta", mensaje = "Hola, tengo una pregunta...")
        SendMessageBody(state) {}
    }
}
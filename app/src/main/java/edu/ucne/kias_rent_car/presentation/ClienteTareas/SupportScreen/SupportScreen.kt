package edu.ucne.kias_rent_car.presentation.SupportTareas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.presentation.Components.KiaBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun SupportScreen(
    viewModel: SupportViewModel = hiltViewModel(),
    onNavigateToSendMessage: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(SupportUiEvent.LoadMensajes)
    }

    SupportBody(
        state = state,
        onEvent = { event ->
            when (event) {
                SupportUiEvent.NavigateToSendMessage -> onNavigateToSendMessage()
                SupportUiEvent.NavigateToHome -> onNavigateToHome()
                SupportUiEvent.NavigateToBookings -> onNavigateToBookings()
                SupportUiEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportBody(
    state: SupportUiState,
    onEvent: (SupportUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SupportUiEvent.NavigateToHome) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        bottomBar = {
            KiaBottomNavigation(
                currentRoute = "support",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onEvent(SupportUiEvent.NavigateToHome)
                        "bookings" -> onEvent(SupportUiEvent.NavigateToBookings)
                        "support" -> { }
                        "profile" -> onEvent(SupportUiEvent.NavigateToProfile)
                    }
                }
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Centro de Ayuda", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(32.dp))

            SupportOption(Icons.AutoMirrored.Filled.Message, "Enviar un Mensaje") {
                onEvent(SupportUiEvent.NavigateToSendMessage)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Contacto Directo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(16.dp))

            SupportOption(Icons.Default.Phone, "Llámanos", "(555)123-4567") { }
            Spacer(modifier = Modifier.height(12.dp))
            SupportOption(Icons.Default.Email, "Email", "soporte@kiasrent.com") { }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Mis Mensajes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = onErrorDark)
                    }
                }
                state.mensajes.isEmpty() -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("No tienes mensajes", color = MaterialTheme.colorScheme.outline, fontSize = 14.sp)
                        }
                    }
                }
                else -> {
                    state.mensajes.forEach { mensaje ->
                        MensajeCard(mensaje.asunto, mensaje.contenido, mensaje.respuesta, mensaje.fechaCreacion)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MensajeCard(asunto: String, contenido: String, respuesta: String?, fecha: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(asunto, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                Text(fecha.take(10), fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Tú:", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            Text(contenido, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 2, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(12.dp))

            if (!respuesta.isNullOrBlank()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Respuesta del Soporte:", fontSize = 12.sp, color = onErrorDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text(respuesta, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Esperando respuesta...", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline, fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
private fun SupportOption(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = onErrorDark, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                subtitle?.let { Text(it, fontSize = 14.sp, color = MaterialTheme.colorScheme.outline) }
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun SupportBodyPreview() {
    MaterialTheme {
        val state = SupportUiState()
        SupportBody(state) {}
    }
}
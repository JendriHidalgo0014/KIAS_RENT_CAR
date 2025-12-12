package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.presentation.Components.AdminBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun AdminMensajesScreen(
    viewModel: AdminMensajesViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToResponder: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToVehiculos: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminMensajesBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is AdminMensajesUiEvent.MensajeClicked -> onNavigateToResponder(event.mensajeId)
                AdminMensajesUiEvent.NavigateBack -> onNavigateBack()
                AdminMensajesUiEvent.NavigateToHome -> onNavigateToHome()
                AdminMensajesUiEvent.NavigateToReservas -> onNavigateToReservas()
                AdminMensajesUiEvent.NavigateToVehiculos -> onNavigateToVehiculos()
                AdminMensajesUiEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMensajesBody(
    state: AdminMensajesUiState,
    onEvent: (AdminMensajesUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KIA'S RENT CAR",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AdminMensajesUiEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        bottomBar = {
            AdminBottomNavigation(
                currentRoute = "admin_mensajes",
                onNavigate = { route ->
                    when (route) {
                        "admin_home" -> onEvent(AdminMensajesUiEvent.NavigateToHome)
                        "admin_reservas" -> onEvent(AdminMensajesUiEvent.NavigateToReservas)
                        "admin_vehiculos" -> onEvent(AdminMensajesUiEvent.NavigateToVehiculos)
                        "admin_profile" -> onEvent(AdminMensajesUiEvent.NavigateToProfile)
                    }
                }
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mensajes de Soporte",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = onErrorDark)
                }
            } else if (state.mensajes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay mensajes", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.mensajes) { mensaje ->
                        MensajeCard(
                            mensaje = mensaje,
                            onClick = { onEvent(AdminMensajesUiEvent.MensajeClicked(mensaje.mensajeId)) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun MensajeCard(
    mensaje: Mensaje,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(onErrorDark),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mensaje.nombreUsuario.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mensaje.nombreUsuario,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = mensaje.asunto,
                    fontSize = 14.sp,
                    color = onErrorDark
                )
                Text(
                    text = mensaje.contenido,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AdminMensajesBodyPreview() {
    MaterialTheme {
        val state = AdminMensajesUiState(
            mensajes = listOf(
                Mensaje(
                    mensajeId = 1,
                    usuarioId = 1,
                    nombreUsuario = "Juan Pérez",
                    asunto = "Consulta sobre reserva",
                    contenido = "Hola, quisiera saber si puedo modificar mi reserva.",
                    respuesta = null,
                    fechaCreacion = "2025-01-10",
                    leido = false
                ),
                Mensaje(
                    mensajeId = 2,
                    usuarioId = 2,
                    nombreUsuario = "María García",
                    asunto = "Problema con el vehículo",
                    contenido = "El aire acondicionado no funciona.",
                    respuesta = "Lo revisaremos.",
                    fechaCreacion = "2025-01-09",
                    leido = true
                )
            )
        )
        AdminMensajesBody(state) {}
    }

}
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.presentation.Components.AdminBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel = hiltViewModel(),
    onNavigateToVehiculos: () -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToUsuarios: () -> Unit,
    onNavigateToMensajes: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                AdminHomeUiEvent.NavigateToVehiculos -> onNavigateToVehiculos()
                AdminHomeUiEvent.NavigateToReservas -> onNavigateToReservas()
                AdminHomeUiEvent.NavigateToUsuarios -> onNavigateToUsuarios()
                AdminHomeUiEvent.NavigateToMensajes -> onNavigateToMensajes()
                AdminHomeUiEvent.NavigateToProfile -> onNavigateToProfile()
            }
        }
    }

    AdminHomeBody(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminHomeBody(
    uiState: AdminHomeUiState,
    onEvent: (AdminHomeUiEvent) -> Unit
) {
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
            AdminBottomNavigation(
                currentRoute = "admin_home",
                onNavigate = { route ->
                    when (route) {
                        "admin_reservas" -> onEvent(AdminHomeUiEvent.NavigateToReservas)
                        "admin_vehiculos" -> onEvent(AdminHomeUiEvent.NavigateToVehiculos)
                        "admin_profile" -> onEvent(AdminHomeUiEvent.NavigateToProfile)
                        else -> Unit
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
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bienvenido ${uiState.nombreAdmin}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "¿Qué quieres hacer?",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            AdminOptionCard(
                icon = Icons.Default.DirectionsCar,
                title = "Gestionar Vehículos",
                subtitle = "Añadir, editar o eliminar vehículos",
                onClick = { onEvent(AdminHomeUiEvent.NavigateToVehiculos) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionCard(
                icon = Icons.Default.CalendarMonth,
                title = "Ver Reservas",
                subtitle = "Consultar reservas activas y pasadas",
                onClick = { onEvent(AdminHomeUiEvent.NavigateToReservas) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionCard(
                icon = Icons.Default.People,
                title = "Administrar Usuarios",
                subtitle = "Gestionar perfiles de clientes",
                onClick = { onEvent(AdminHomeUiEvent.NavigateToUsuarios) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionCard(
                icon = Icons.AutoMirrored.Filled.Message,
                title = "Mensajes de Soporte",
                subtitle = "Revisar y responder consultas",
                onClick = { onEvent(AdminHomeUiEvent.NavigateToMensajes) }
            )
        }
    }
}

@Composable
private fun AdminOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = onErrorDark,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AdminHomeScreenBodyPreview() {
    MaterialTheme {
        AdminHomeBody(
            uiState = AdminHomeUiState(nombreAdmin = "Carlos"),
            onEvent = {}
        )
    }
}
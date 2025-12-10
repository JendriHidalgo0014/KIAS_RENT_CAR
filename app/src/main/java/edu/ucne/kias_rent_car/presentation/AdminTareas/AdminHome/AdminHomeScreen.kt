package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.kias_rent_car.presentation.Components.AdminBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    onNavigateToVehiculos: () -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToUsuarios: () -> Unit,
    onNavigateToMensajes: () -> Unit,
    onNavigateToProfile: () -> Unit
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
                        "admin_home" -> { }
                        "admin_reservas" -> onNavigateToReservas()
                        "admin_vehiculos" -> onNavigateToVehiculos()
                        "admin_profile" -> onNavigateToProfile()
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
                text = "Bienvenido Admin!",
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

            // Opciones
            AdminOptionCard(
                icon = Icons.Default.DirectionsCar,
                title = "Gestionar Vehículos",
                subtitle = "Añadir, editar o eliminar vehículos",
                onClick = onNavigateToVehiculos
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionCard(
                icon = Icons.Default.CalendarMonth,
                title = "Ver Reservas",
                subtitle = "Consultar reservas activas y pasadas",
                onClick = onNavigateToReservas
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionCard(
                icon = Icons.Default.People,
                title = "Administrar Usuarios",
                subtitle = "Gestionar perfiles de clientes",
                onClick = onNavigateToUsuarios
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionCard(
                icon = Icons.AutoMirrored.Filled.Message,
                title = "Mensajes de Soporte",
                subtitle = "Revisar y responder consultas",
                onClick = onNavigateToMensajes
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
private fun AdminHomeScreenPreview() {
    AdminHomeScreen(
        onNavigateToVehiculos = {},
        onNavigateToReservas = {},
        onNavigateToUsuarios = {},
        onNavigateToMensajes = {},
        onNavigateToProfile = {}
    )
}
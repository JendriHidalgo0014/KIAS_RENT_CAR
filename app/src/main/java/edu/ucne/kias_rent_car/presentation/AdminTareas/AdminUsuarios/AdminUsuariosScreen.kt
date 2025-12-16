package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.presentation.Components.*
import edu.ucne.kias_rent_car.ui.theme.onErrorDark

@Composable
fun AdminUsuariosScreen(
    viewModel: AdminUsuariosViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToVehiculos: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminUsuariosBody(
        state = state,
        onEvent = { event ->
            when (event) {
                AdminUsuariosUiEvent.NavigateBack -> onNavigateBack()
                AdminUsuariosUiEvent.NavigateToHome -> onNavigateToHome()
                AdminUsuariosUiEvent.NavigateToReservas -> onNavigateToReservas()
                AdminUsuariosUiEvent.NavigateToVehiculos -> onNavigateToVehiculos()
                AdminUsuariosUiEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun AdminUsuariosBody(
    state: AdminUsuariosUiState,
    onEvent: (AdminUsuariosUiEvent) -> Unit
) {
    KiaScaffold(
        onNavigateBack = { onEvent(AdminUsuariosUiEvent.NavigateBack) },
        bottomBar = {
            AdminBottomNavigation(
                currentRoute = "admin_usuarios",
                onNavigate = { route ->
                    when (route) {
                        "admin_home" -> onEvent(AdminUsuariosUiEvent.NavigateToHome)
                        "admin_reservas" -> onEvent(AdminUsuariosUiEvent.NavigateToReservas)
                        "admin_vehiculos" -> onEvent(AdminUsuariosUiEvent.NavigateToVehiculos)
                        "admin_profile" -> onEvent(AdminUsuariosUiEvent.NavigateToProfile)
                        else -> Unit
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gestión de Usuarios",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            KiaSearchField(
                value = state.searchQuery,
                onValueChange = { onEvent(AdminUsuariosUiEvent.OnSearchChange(it)) },
                placeholder = "Buscar usuario..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                KiaLoadingBox()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.usuarios) { usuario ->
                        UsuarioCard(
                            usuario = usuario,
                            onDelete = { onEvent(AdminUsuariosUiEvent.DeleteUsuario(usuario.id)) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun UsuarioCard(
    usuario: Usuario,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(onErrorDark),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = usuario.nombre.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuario.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = usuario.rol,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar", tint = onErrorDark)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AdminUsuariosBodyPreview() {
    MaterialTheme {
        AdminUsuariosBody(AdminUsuariosUiState()) {}
    }
}
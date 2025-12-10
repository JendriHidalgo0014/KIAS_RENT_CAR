package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import edu.ucne.kias_rent_car.presentation.Components.AdminBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminVehiculosScreen(
    viewModel: AdminVehiculosViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddVehiculo: () -> Unit,
    onNavigateToEditVehiculo: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddVehiculo,
                containerColor = onErrorDark
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            AdminBottomNavigation(
                currentRoute = "admin_vehiculos",
                onNavigate = { route ->
                    when (route) {
                        "admin_home" -> onNavigateToHome()
                        "admin_reservas" -> onNavigateToReservas()
                        "admin_vehiculos" -> { }
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gestión de Vehículos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Barra de búsqueda
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchChanged(it) },
                placeholder = { Text("Buscar por modelo o matrícula", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = onErrorDark,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = onErrorDark)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.vehiculos) { vehiculo ->
                        AdminVehiculoCard(
                            vehiculo = vehiculo,
                            onEdit = { onNavigateToEditVehiculo(vehiculo.id) },
                            onDelete = { viewModel.deleteVehiculo(vehiculo.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
@Composable
private fun AdminVehiculoCard(
    vehiculo: Vehicle,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(vehiculo.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = vehiculo.modelo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2D2D2D))
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2D2D2D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vehiculo.modelo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Matrícula: ABC-123",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = if (vehiculo.disponible) "Estado: Disponible" else "Estado: No disponible",
                    fontSize = 12.sp,
                    color = if (vehiculo.disponible) Color(0xFF4CAF50) else onErrorDark
                )
            }
            // Botones de acción
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.White
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = onErrorDark
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AdminVehiculosScreenPreview() {
    val vehiculosEjemplo = listOf(
        Vehicle(
            id = "1",
            remoteId = 1,
            modelo = "Kia Sportage",
            descripcion = "SUV familiar",
            categoria = edu.ucne.kias_rent_car.domain.model.VehicleCategory.SUV,
            asientos = 5,
            transmision = edu.ucne.kias_rent_car.domain.model.TransmisionType.AUTOMATIC,
            precioPorDia = 120.0,
            imagenUrl = "",
            disponible = true
        ),
        Vehicle(
            id = "2",
            remoteId = 2,
            modelo = "Kia K5",
            descripcion = "Sedan deportivo",
            categoria = edu.ucne.kias_rent_car.domain.model.VehicleCategory.SEDAN,
            asientos = 5,
            transmision = edu.ucne.kias_rent_car.domain.model.TransmisionType.AUTOMATIC,
            precioPorDia = 100.0,
            imagenUrl = "",
            disponible = false
        )
    )

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
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = onErrorDark
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
            }
        },
        containerColor = scrimLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gestión de Vehículos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar por modelo o matrícula", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = onErrorDark,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vehiculosEjemplo) { vehiculo ->
                    AdminVehiculoCard(
                        vehiculo = vehiculo,
                        onEdit = {},
                        onDelete = {}
                    )
                }
            }
        }
    }
}
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsCar
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
import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.presentation.Components.AdminBottomNavigation
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun AdminReservasScreen(
    viewModel: AdminReservasViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToModifyEstado: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToVehiculos: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminReservasBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is AdminReservasEvent.ReservaClicked -> onNavigateToModifyEstado(event.reservacionId)
                is AdminReservasEvent.NavigateBack -> onNavigateBack()
                is AdminReservasEvent.NavigateToHome -> onNavigateToHome()
                is AdminReservasEvent.NavigateToVehiculos -> onNavigateToVehiculos()
                is AdminReservasEvent.NavigateToProfile -> onNavigateToProfile()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReservasBody(
    state: AdminReservasUiState,
    onEvent: (AdminReservasEvent) -> Unit
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
                    IconButton(onClick = { onEvent(AdminReservasEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        bottomBar = {
            AdminBottomNavigation(
                currentRoute = "admin_reservas",
                onNavigate = { route ->
                    when (route) {
                        "admin_home" -> onEvent(AdminReservasEvent.NavigateToHome)
                        "admin_reservas" -> { }
                        "admin_vehiculos" -> onEvent(AdminReservasEvent.NavigateToVehiculos)
                        "admin_profile" -> onEvent(AdminReservasEvent.NavigateToProfile)
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
                text = "Estado de Reservas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(EstadoReserva.FILTROS_ADMIN) { filtro ->
                    FilterChip(
                        selected = state.filtroActual == filtro,
                        onClick = { onEvent(AdminReservasEvent.FiltroChanged(filtro)) },
                        label = { Text(filtro) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = onErrorDark,
                            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = Color(0xFF2D2D2D),
                            labelColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }

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
                    items(state.reservaciones) { reservacion ->
                        AdminReservaCard(
                            reservacion = reservacion,
                            onClick = { onEvent(AdminReservasEvent.ReservaClicked(reservacion.reservacionId)) }
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
private fun AdminReservaCard(
    reservacion: Reservacion,
    onClick: () -> Unit
) {
    val estadoColor = when (reservacion.estado) {
        EstadoReserva.CONFIRMADA, EstadoReserva.RESERVADO -> Color(0xFF4CAF50)
        EstadoReserva.EN_PROCESO_SIN_ESPACIO, EstadoReserva.EN_PROCESO -> Color(0xFFFF9800)
        EstadoReserva.FINALIZADA -> Color.Gray
        EstadoReserva.CANCELADA -> onErrorDark
        else -> Color.Gray
    }

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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(reservacion.vehiculo?.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
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
                    text = reservacion.vehiculo?.modelo ?: "Vehículo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(estadoColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = reservacion.estado,
                        fontSize = 12.sp,
                        color = estadoColor
                    )
                }

                Text(
                    text = "${reservacion.usuario?.nombre ?: "Cliente"} | ${reservacion.fechaRecogida} - ${reservacion.fechaDevolucion}",
                    fontSize = 11.sp,
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
private fun AdminReservasBodyPreview() {
    MaterialTheme {
        val state = AdminReservasUiState(
            isLoading = false,
            filtroActual = EstadoReserva.TODOS,
            reservaciones = listOf(
                Reservacion(
                    reservacionId = 1,
                    usuarioId = 1,
                    vehiculoId = 1,
                    fechaRecogida = "2025-01-15",
                    horaRecogida = "10:00",
                    fechaDevolucion = "2025-01-20",
                    horaDevolucion = "10:00",
                    ubicacionRecogidaId = 1,
                    ubicacionDevolucionId = 1,
                    estado = EstadoReserva.CONFIRMADA,
                    subtotal = 500.0,
                    impuestos = 90.0,
                    total = 590.0,
                    codigoReserva = "KR-123456",
                    fechaCreacion = "2025-01-10",
                    usuario = Usuario(id = 1, nombre = "Juan Pérez", email = "juan@test.com", telefono = null, rol = "Cliente"),
                    vehiculo = null
                ),
                Reservacion(
                    reservacionId = 2,
                    usuarioId = 2,
                    vehiculoId = 2,
                    fechaRecogida = "2025-01-18",
                    horaRecogida = "09:00",
                    fechaDevolucion = "2025-01-22",
                    horaDevolucion = "09:00",
                    ubicacionRecogidaId = 1,
                    ubicacionDevolucionId = 1,
                    estado = EstadoReserva.EN_PROCESO,
                    subtotal = 600.0,
                    impuestos = 108.0,
                    total = 708.0,
                    codigoReserva = "KR-789012",
                    fechaCreacion = "2025-01-12",
                    usuario = Usuario(id = 2, nombre = "María García", email = "maria@test.com", telefono = null, rol = "Cliente"),
                    vehiculo = null
                )
            )
        )

        AdminReservasBody(state) {}
    }
}
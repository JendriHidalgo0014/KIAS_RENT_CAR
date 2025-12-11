package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.domain.model.Reservacion

data class AdminReservasUiState(
    val reservaciones: List<Reservacion> = emptyList(),
    val filtroActual: String = EstadoReserva.TODOS,
    val isLoading: Boolean = false,
    val error: String? = null
)
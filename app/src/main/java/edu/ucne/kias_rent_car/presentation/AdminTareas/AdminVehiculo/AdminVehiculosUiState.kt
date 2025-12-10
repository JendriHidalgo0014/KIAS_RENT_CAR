package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import edu.ucne.kias_rent_car.domain.model.Vehicle

data class AdminVehiculosUiState(
    val vehiculos: List<Vehicle> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
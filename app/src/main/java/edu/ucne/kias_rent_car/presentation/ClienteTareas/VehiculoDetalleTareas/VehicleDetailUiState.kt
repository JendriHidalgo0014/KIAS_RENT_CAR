package edu.ucne.kias_rent_car.presentation.ClienteTareas.VehiculoDetalleTareas

import edu.ucne.kias_rent_car.domain.model.Vehicle

data class VehicleDetailUiState(
    val vehicle: Vehicle? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
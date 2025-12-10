package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory

data class HomeUiState(
    val vehicles: List<Vehicle> = emptyList(),
    val selectedCategory: VehicleCategory = VehicleCategory.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

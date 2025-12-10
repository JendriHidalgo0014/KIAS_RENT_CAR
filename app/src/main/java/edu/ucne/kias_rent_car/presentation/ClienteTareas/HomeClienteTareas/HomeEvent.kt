package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

import edu.ucne.kias_rent_car.domain.model.VehicleCategory

sealed class HomeEvent {
    data class OnCategorySelected(val category: VehicleCategory) : HomeEvent()
    data class OnSearchQueryChanged(val query: String) : HomeEvent()
    data class OnVehicleClicked(val vehicleId: String) : HomeEvent()
    object OnRefresh : HomeEvent()
    object OnErrorDismissed : HomeEvent()
}
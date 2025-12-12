package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

import edu.ucne.kias_rent_car.domain.model.VehicleCategory

sealed interface HomeUiEvent {
    data class OnCategorySelected(val category: VehicleCategory) : HomeUiEvent
    data class OnSearchQueryChanged(val query: String) : HomeUiEvent
    data class OnVehicleClicked(val vehicleId: String) : HomeUiEvent
    object OnRefresh : HomeUiEvent
    object OnErrorDismissed : HomeUiEvent
    object NavigateToBookings : HomeUiEvent
    object NavigateToSupport : HomeUiEvent
    object NavigateToProfile : HomeUiEvent
}
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

sealed interface AdminVehiculosUiEvent {
    data class OnSearchChange(val query: String) : AdminVehiculosUiEvent
    data class DeleteVehiculo(val id: String) : AdminVehiculosUiEvent
    data class EditVehiculo(val id: String) : AdminVehiculosUiEvent
    object NavigateBack : AdminVehiculosUiEvent
    object NavigateToAddVehiculo : AdminVehiculosUiEvent
    object NavigateToHome : AdminVehiculosUiEvent
    object NavigateToReservas : AdminVehiculosUiEvent
    object NavigateToProfile : AdminVehiculosUiEvent
}
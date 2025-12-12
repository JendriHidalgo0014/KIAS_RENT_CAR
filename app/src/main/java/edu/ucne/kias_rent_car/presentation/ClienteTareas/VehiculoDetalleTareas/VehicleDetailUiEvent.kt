package edu.ucne.kias_rent_car.presentation.ClienteTareas.VehiculoDetalleTareas

sealed interface VehicleDetailUiEvent {
    data class LoadVehicle(val vehicleId: String) : VehicleDetailUiEvent
    data class NavigateToReservation(val vehicleId: String) : VehicleDetailUiEvent
    object NavigateBack : VehicleDetailUiEvent
}
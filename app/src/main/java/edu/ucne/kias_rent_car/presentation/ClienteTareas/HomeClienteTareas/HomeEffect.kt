package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

sealed class HomeEffect {
    data class NavigateToVehicleDetail(val vehicleId: String) : HomeEffect()
    data class ShowError(val message: String) : HomeEffect()
}
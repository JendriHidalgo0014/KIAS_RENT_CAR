package edu.ucne.kias_rent_car.presentation.ProfileTareas

sealed interface AdminProfileUiEvent {
    object Logout : AdminProfileUiEvent
    object NavigateToHome : AdminProfileUiEvent
    object NavigateToReservas : AdminProfileUiEvent
    object NavigateToVehiculos : AdminProfileUiEvent
}
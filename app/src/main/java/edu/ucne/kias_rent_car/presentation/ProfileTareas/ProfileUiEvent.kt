package edu.ucne.kias_rent_car.presentation.ProfileTareas

sealed interface ProfileUiEvent {
    object Logout : ProfileUiEvent
    object NavigateToHome : ProfileUiEvent
    object NavigateToBookings : ProfileUiEvent
    object NavigateToSupport : ProfileUiEvent
}
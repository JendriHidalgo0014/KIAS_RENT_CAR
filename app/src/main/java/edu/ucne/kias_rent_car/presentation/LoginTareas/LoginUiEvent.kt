package edu.ucne.kias_rent_car.presentation.LoginTareas

sealed interface LoginUiEvent {
    data class OnEmailChange(val email: String) : LoginUiEvent
    data class OnPasswordChange(val password: String) : LoginUiEvent
    object TogglePasswordVisibility : LoginUiEvent
    object Login : LoginUiEvent
    object UserMessageShown : LoginUiEvent
}
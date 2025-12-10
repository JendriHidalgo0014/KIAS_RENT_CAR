package edu.ucne.kias_rent_car.presentation.LoginTareas

sealed class LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent()  // ← Debe ser 'EmailChanged'
    data class PasswordChanged(val password: String) : LoginUiEvent()
    data object Login : LoginUiEvent()
    data object TogglePasswordVisibility : LoginUiEvent()
    data object ClearError : LoginUiEvent()
}
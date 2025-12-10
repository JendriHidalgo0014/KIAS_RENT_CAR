package edu.ucne.kias_rent_car.presentation.LoginTareas

sealed class RegistroUiEvent {
    data class NombreChanged(val nombre: String) : RegistroUiEvent()
    data class EmailChanged(val email: String) : RegistroUiEvent()
    data class TelefonoChanged(val telefono: String) : RegistroUiEvent()
    data class PasswordChanged(val password: String) : RegistroUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistroUiEvent()
    data object TogglePasswordVisibility : RegistroUiEvent()
    data object Registrar : RegistroUiEvent()
    data object ClearError : RegistroUiEvent()
}
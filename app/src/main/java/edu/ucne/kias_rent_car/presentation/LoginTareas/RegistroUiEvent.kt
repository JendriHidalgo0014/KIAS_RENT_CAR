package edu.ucne.kias_rent_car.presentation.LoginTareas

sealed interface RegistroUiEvent {
    data class OnNombreChange(val nombre: String) : RegistroUiEvent
    data class OnEmailChange(val email: String) : RegistroUiEvent
    data class OnTelefonoChange(val telefono: String) : RegistroUiEvent
    data class OnPasswordChange(val password: String) : RegistroUiEvent
    data class OnConfirmPasswordChange(val confirmPassword: String) : RegistroUiEvent
    object TogglePasswordVisibility : RegistroUiEvent
    object Registrar : RegistroUiEvent
    object UserMessageShown : RegistroUiEvent
}
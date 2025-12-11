package edu.ucne.kias_rent_car.presentation.LoginTareas

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val registroExitoso: Boolean = false,
    val userMessage: String? = null,
    val emailError: String? = null
)
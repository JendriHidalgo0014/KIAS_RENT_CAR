package edu.ucne.kias_rent_car.presentation.LoginTareas

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val emailError: String? = null,
    val telefono: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false
) {
    fun esFormularioValido(): Boolean {
        return nombre.isNotBlank() &&
                email.isNotBlank() &&
                emailError == null &&
                password.length >= 4 &&
                password == confirmPassword
    }
}
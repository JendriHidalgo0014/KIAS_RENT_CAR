package edu.ucne.kias_rent_car.presentation

data class RegistroUiState(
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false
) {
    fun esFormularioValido(): Boolean {
        return userName.isNotBlank() &&
                password.length >= 4 &&
                password == confirmPassword
    }
}

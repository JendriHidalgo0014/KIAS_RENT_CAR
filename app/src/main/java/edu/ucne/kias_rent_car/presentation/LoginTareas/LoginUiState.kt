package edu.ucne.kias_rent_car.presentation.LoginTareas

import edu.ucne.kias_rent_car.domain.model.Usuario

data class LoginUIState(
    val email: String = "",           // ← Debe ser 'email', no 'userName'
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginExitoso: Usuario? = null
) {
    fun esFormularioValido(): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }
}
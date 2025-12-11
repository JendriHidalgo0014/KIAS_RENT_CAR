package edu.ucne.kias_rent_car.presentation.LoginTareas

import edu.ucne.kias_rent_car.domain.model.Usuario

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val loginExitoso: Usuario? = null
)
package edu.ucne.kias_rent_car.presentation.ProfileTareas

data class ProfileUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val logoutSuccess: Boolean = false,
    val isLoading: Boolean = false
)
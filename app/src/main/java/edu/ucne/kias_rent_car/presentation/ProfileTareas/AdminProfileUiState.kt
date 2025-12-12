package edu.ucne.kias_rent_car.presentation.ProfileTareas

data class AdminProfileUiState(
    val nombre: String = "",
    val email: String = "",
    val rol: String = "",
    val isLoading: Boolean = false
)
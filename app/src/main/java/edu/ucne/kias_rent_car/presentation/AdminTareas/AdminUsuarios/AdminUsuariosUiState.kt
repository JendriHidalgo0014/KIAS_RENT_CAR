package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios

import edu.ucne.kias_rent_car.domain.model.Usuario

data class AdminUsuariosUiState(
    val usuarios: List<Usuario> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
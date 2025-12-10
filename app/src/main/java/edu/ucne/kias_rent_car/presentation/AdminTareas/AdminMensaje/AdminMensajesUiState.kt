package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import edu.ucne.kias_rent_car.domain.model.Mensaje

data class AdminMensajesUiState(
    val mensajes: List<Mensaje> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
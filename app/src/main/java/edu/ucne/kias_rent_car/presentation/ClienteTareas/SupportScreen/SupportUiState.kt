package edu.ucne.kias_rent_car.presentation.SupportTareas

import edu.ucne.kias_rent_car.domain.model.Mensaje

data class SupportUiState(
    val mensajes: List<Mensaje> = emptyList(),
    val isLoading: Boolean = false
)
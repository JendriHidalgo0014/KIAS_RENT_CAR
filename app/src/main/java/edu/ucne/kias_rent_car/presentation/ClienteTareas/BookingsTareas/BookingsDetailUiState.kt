package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import edu.ucne.kias_rent_car.domain.model.Reservacion

data class BookingDetailUiState(
    val reservacion: Reservacion? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
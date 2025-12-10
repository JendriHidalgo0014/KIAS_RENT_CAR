package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import edu.ucne.kias_rent_car.domain.model.Reservacion

data class BookingsUiState(
    val reservaciones: List<Reservacion> = emptyList(),
    val filtro: BookingFilter = BookingFilter.ACTUALES,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class BookingFilter {
    ACTUALES,
    PASADAS
}
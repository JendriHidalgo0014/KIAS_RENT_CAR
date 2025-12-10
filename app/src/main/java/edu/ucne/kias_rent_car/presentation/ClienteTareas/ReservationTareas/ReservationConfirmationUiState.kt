package edu.ucne.kias_rent_car.presentation.ReservationTareas

import edu.ucne.kias_rent_car.domain.model.Vehicle

data class ReservationConfirmationUiState(
    val vehicle: Vehicle? = null,
    val fechaRecogida: String = "",
    val fechaDevolucion: String = "",
    val lugarRecogida: String = "",
    val lugarDevolucion: String = "",
    val dias: Int = 0,
    val subtotal: Double = 0.0,
    val impuestos: Double = 0.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
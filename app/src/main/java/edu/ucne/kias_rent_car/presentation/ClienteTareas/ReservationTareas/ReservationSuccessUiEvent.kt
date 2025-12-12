package edu.ucne.kias_rent_car.presentation.ReservationTareas

sealed interface ReservationSuccessUiEvent {
    data class LoadReservacion(val reservacionId: String) : ReservationSuccessUiEvent
    data class VerDetalles(val reservacionId: String) : ReservationSuccessUiEvent
    object VolverInicio : ReservationSuccessUiEvent
}
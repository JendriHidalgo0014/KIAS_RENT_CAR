package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

sealed class BookingsEvent {
    data class FilterChanged(val filter: BookingFilter) : BookingsEvent()
    data object Refresh : BookingsEvent()
}
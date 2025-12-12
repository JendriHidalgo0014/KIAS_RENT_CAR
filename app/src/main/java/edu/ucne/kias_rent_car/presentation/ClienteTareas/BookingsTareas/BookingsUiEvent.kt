package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

sealed interface BookingsUiEvent {
    data class FilterChanged(val filter: BookingFilter) : BookingsUiEvent
    data class NavigateToDetail(val bookingId: Int) : BookingsUiEvent
    object Refresh : BookingsUiEvent
    object NavigateToHome : BookingsUiEvent
    object NavigateToSupport : BookingsUiEvent
    object NavigateToProfile : BookingsUiEvent
}
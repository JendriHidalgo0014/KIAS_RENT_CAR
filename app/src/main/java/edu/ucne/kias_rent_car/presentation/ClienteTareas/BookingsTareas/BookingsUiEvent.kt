package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

sealed interface BookingsUiEvent {
    data class NavigateToDetail(val bookingId: String) : BookingsUiEvent
    data class FilterChanged(val filter: BookingFilter) : BookingsUiEvent
    data object NavigateToHome : BookingsUiEvent
    data object NavigateToSupport : BookingsUiEvent
    data object NavigateToProfile : BookingsUiEvent
}
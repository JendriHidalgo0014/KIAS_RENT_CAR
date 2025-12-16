package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

sealed interface BookingDetailUiEvent {
    data class LoadBooking(val bookingId: String) : BookingDetailUiEvent
    data class NavigateToModify(val bookingId: String) : BookingDetailUiEvent
    data object NavigateBack : BookingDetailUiEvent
    data object NavigateToHome : BookingDetailUiEvent
    data object NavigateToBookings : BookingDetailUiEvent
    data object NavigateToSupport : BookingDetailUiEvent
    data object NavigateToProfile : BookingDetailUiEvent
    data object CancelarReserva : BookingDetailUiEvent
}
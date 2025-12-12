package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

sealed interface BookingDetailUiEvent {
    data class LoadBooking(val bookingId: Int) : BookingDetailUiEvent
    data class NavigateToModify(val bookingId: Int) : BookingDetailUiEvent
    object CancelarReserva : BookingDetailUiEvent
    object NavigateBack : BookingDetailUiEvent
    object NavigateToHome : BookingDetailUiEvent
    object NavigateToBookings : BookingDetailUiEvent
    object NavigateToSupport : BookingDetailUiEvent
    object NavigateToProfile : BookingDetailUiEvent
}
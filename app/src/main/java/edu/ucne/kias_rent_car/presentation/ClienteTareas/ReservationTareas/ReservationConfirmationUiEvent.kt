package edu.ucne.kias_rent_car.presentation.ReservationTareas

sealed interface ReservationConfirmationUiEvent {
    data class LoadData(val vehicleId: String) : ReservationConfirmationUiEvent
    data class ConfirmarReserva(val vehicleId: String) : ReservationConfirmationUiEvent
    object Cancelar : ReservationConfirmationUiEvent
    object NavigateBack : ReservationConfirmationUiEvent
}
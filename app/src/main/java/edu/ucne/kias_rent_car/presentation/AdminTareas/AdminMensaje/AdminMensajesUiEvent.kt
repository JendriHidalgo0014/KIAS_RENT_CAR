package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

sealed interface AdminMensajesUiEvent {
    object Refresh : AdminMensajesUiEvent
    data class MensajeClicked(val mensajeId: Int) : AdminMensajesUiEvent
    object NavigateBack : AdminMensajesUiEvent
    object NavigateToHome : AdminMensajesUiEvent
    object NavigateToReservas : AdminMensajesUiEvent
    object NavigateToVehiculos : AdminMensajesUiEvent
    object NavigateToProfile : AdminMensajesUiEvent
}
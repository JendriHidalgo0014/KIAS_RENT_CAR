package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

sealed interface ResponderMensajeUiEvent {
    data class LoadMensaje(val mensajeId: String) : ResponderMensajeUiEvent
    data class OnRespuestaChange(val respuesta: String) : ResponderMensajeUiEvent
    data object EnviarRespuesta : ResponderMensajeUiEvent
    data object NavigateBack : ResponderMensajeUiEvent
}
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

sealed interface ResponderMensajeUiEvent {
    data class LoadMensaje(val mensajeId: Int) : ResponderMensajeUiEvent
    data class OnRespuestaChange(val respuesta: String) : ResponderMensajeUiEvent
    object EnviarRespuesta : ResponderMensajeUiEvent
    object NavigateBack : ResponderMensajeUiEvent
}
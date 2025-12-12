package edu.ucne.kias_rent_car.presentation.SupportTareas

sealed interface SendMessageUiEvent {
    data class OnAsuntoChange(val asunto: String) : SendMessageUiEvent
    data class OnMensajeChange(val mensaje: String) : SendMessageUiEvent
    object EnviarMensaje : SendMessageUiEvent
    object NavigateBack : SendMessageUiEvent
}
package edu.ucne.kias_rent_car.presentation.SupportTareas

sealed interface SupportUiEvent {
    object LoadMensajes : SupportUiEvent
    object NavigateToSendMessage : SupportUiEvent
    object NavigateToHome : SupportUiEvent
    object NavigateToBookings : SupportUiEvent
    object NavigateToProfile : SupportUiEvent
}
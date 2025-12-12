package edu.ucne.kias_rent_car.presentation.SupportTareas

data class SendMessageUiState(
    val asunto: String = "",
    val mensaje: String = "",
    val isLoading: Boolean = false,
    val messageSent: Boolean = false
)
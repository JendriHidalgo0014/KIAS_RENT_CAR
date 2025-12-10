package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

data class ResponderMensajeUiState(
    val mensajeId: Int = 0,
    val nombreUsuario: String = "",
    val mensajeOriginal: String = "",
    val respuesta: String = "",
    val isLoading: Boolean = false,
    val enviado: Boolean = false
)
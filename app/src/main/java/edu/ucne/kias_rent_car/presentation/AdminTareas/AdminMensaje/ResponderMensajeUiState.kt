package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

data class ResponderMensajeUiState(
    val mensajeId: String = "",
    val nombreUsuario: String = "",
    val mensajeOriginal: String = "",
    val respuesta: String = "",
    val isLoading: Boolean = false,
    val enviado: Boolean = false,
    val error: String? = null
)
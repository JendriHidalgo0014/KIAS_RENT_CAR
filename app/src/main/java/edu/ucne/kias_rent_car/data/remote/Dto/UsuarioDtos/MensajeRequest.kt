package edu.ucne.kias_rent_car.data.remote.dto

data class MensajeRequest(
    val usuarioId: Int,
    val asunto: String,
    val contenido: String
)
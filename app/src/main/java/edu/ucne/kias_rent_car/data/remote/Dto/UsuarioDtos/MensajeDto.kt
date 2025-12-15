package edu.ucne.kias_rent_car.data.remote.dto

data class MensajeDto(
    val mensajeId: Int,
    val usuarioId: Int,
    val nombreUsuario: String? = null,
    val asunto: String,
    val contenido: String,
    val respuesta: String? = null,
    val fechaCreacion: String? = null,
    val leido: Boolean? = false
)
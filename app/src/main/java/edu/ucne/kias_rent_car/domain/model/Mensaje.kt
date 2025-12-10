package edu.ucne.kias_rent_car.domain.model

data class Mensaje(
    val mensajeId: Int,
    val usuarioId: Int,
    val nombreUsuario: String,
    val asunto: String,
    val contenido: String,
    val respuesta: String? = null,
    val fechaCreacion: String,
    val leido: Boolean = false
)
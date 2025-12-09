package edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos

import com.squareup.moshi.Json

data class MensajeDto(
    @Json(name = "mensajeId")
    val mensajeId: Int,
    @Json(name = "usuarioId")
    val usuarioId: Int,
    @Json(name = "nombreUsuario")
    val nombreUsuario: String,
    @Json(name = "asunto")
    val asunto: String,
    @Json(name = "contenido")
    val contenido: String,
    @Json(name = "respuesta")
    val respuesta: String?,
    @Json(name = "fechaCreacion")
    val fechaCreacion: String?,
    @Json(name = "leido")
    val leido: Boolean
)

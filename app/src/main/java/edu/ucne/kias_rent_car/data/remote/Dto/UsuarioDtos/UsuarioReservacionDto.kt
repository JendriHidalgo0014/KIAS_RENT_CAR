package edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos

import com.squareup.moshi.Json

data class UsuarioReservacionDto(
    @Json(name = "usuarioId")
    val usuarioId: Int,

    @Json(name = "nombre")
    val nombre: String,

    @Json(name = "email")
    val email: String
)

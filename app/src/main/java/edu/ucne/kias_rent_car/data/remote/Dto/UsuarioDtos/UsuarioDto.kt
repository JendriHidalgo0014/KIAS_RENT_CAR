package edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos

import com.squareup.moshi.Json

data class UsuarioDto(
    @Json(name = "usuarioId")
    val usuarioId: Int,

    @Json(name = "nombre")
    val nombre: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String?,

    @Json(name = "telefono")
    val telefono: String?,

    @Json(name = "rol")
    val rol: String,

    @Json(name = "fechaRegistro")
    val fechaRegistro: String?
)
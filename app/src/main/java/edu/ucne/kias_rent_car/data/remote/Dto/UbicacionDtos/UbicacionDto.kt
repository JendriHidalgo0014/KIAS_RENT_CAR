package edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos

import com.squareup.moshi.Json

data class UbicacionDto(
    @Json(name = "ubicacionId")
    val ubicacionId: Int,

    @Json(name = "nombre")
    val nombre: String,

    @Json(name = "direccion")
    val direccion: String?,

    @Json(name = "activa")
    val activa: Boolean
)
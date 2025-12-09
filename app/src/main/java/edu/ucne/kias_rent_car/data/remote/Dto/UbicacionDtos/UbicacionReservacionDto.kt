package edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos

import com.squareup.moshi.Json

data class UbicacionReservacionDto(
    @Json(name = "ubicacionId")
    val ubicacionId: Int,
    @Json(name = "nombre")
    val nombre: String
)
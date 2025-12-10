package edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos

import com.squareup.moshi.Json

data class VehiculoReservacionDto(
    @Json(name = "vehiculoId")
    val vehiculoId: Int,

    @Json(name = "modelo")
    val modelo: String,

    @Json(name = "imagenUrl")
    val imagenUrl: String?,

    @Json(name = "precioPorDia")
    val precioPorDia: Double? = null
)
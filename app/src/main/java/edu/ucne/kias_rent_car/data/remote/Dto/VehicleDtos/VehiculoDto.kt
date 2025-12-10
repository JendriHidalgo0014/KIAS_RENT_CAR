package edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos

import com.squareup.moshi.Json

data class VehiculoDto(
    @Json(name = "vehiculoId")
    val vehiculoId: Int,

    @Json(name = "modelo")
    val modelo: String,

    @Json(name = "descripcion")
    val descripcion: String?,

    @Json(name = "categoria")
    val categoria: String,

    @Json(name = "asientos")
    val asientos: Int,

    @Json(name = "transmision")
    val transmision: String,

    @Json(name = "precioPorDia")
    val precioPorDia: Double,

    @Json(name = "imagenUrl")
    val imagenUrl: String?,

    @Json(name = "disponible")
    val disponible: Boolean,

    @Json(name = "fechaIngreso")
    val fechaIngreso: String?
)

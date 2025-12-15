package edu.ucne.kias_rent_car.data.remote.dto

data class VehiculoDto(
    val vehiculoId: Int,
    val modelo: String,
    val descripcion: String? = null,
    val categoria: String? = null,
    val asientos: Int? = null,
    val transmision: String? = null,
    val precioPorDia: Double,
    val imagenUrl: String? = null,
    val disponible: Boolean? = true,
    val fechaIngreso: String? = null
)
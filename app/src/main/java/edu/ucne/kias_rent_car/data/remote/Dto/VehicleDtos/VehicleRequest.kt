package edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos

data class VehiculoRequest(
    val modelo: String,
    val descripcion: String?,
    val categoria: String,
    val asientos: Int,
    val transmision: String,
    val precioPorDia: Double,
    val imagenUrl: String?,
    val disponible: Boolean = true
)
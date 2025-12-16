package edu.ucne.kias_rent_car.domain.model

data class VehicleParams(
    val id: String? = null,
    val modelo: String,
    val descripcion: String,
    val categoria: String,
    val asientos: Int,
    val transmision: String,
    val precioPorDia: Double,
    val imagenUrl: String
)
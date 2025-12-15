package edu.ucne.kias_rent_car.data.remote.dto

data class VehiculoRequest(
    val modelo: String,
    val descripcion: String? = null,
    val categoria: String,
    val asientos: Int,
    val transmision: String,
    val precioPorDia: Double,
    val imagenUrl: String? = null,
    val disponible: Boolean = true
)

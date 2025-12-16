package edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos


data class VehiculoReservacionDto(
    val vehiculoId: Int,
    val modelo: String,
    val imagenUrl: String?,
    val precioPorDia: Double? = null
)
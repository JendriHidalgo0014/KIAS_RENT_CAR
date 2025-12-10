package edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos

data class UbicacionRequest(
    val nombre: String,
    val direccion: String? = null
)
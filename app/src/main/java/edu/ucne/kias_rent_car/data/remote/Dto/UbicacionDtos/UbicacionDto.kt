package edu.ucne.kias_rent_car.data.remote.dto

data class UbicacionDto(
    val ubicacionId: Int,
    val nombre: String,
    val direccion: String? = null,
    val activa: Boolean? = true
)
package edu.ucne.kias_rent_car.domain.model

import java.util.UUID

data class Ubicacion(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val direccion: String? = null
)
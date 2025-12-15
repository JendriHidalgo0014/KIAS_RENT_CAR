package edu.ucne.kias_rent_car.domain.model

import java.util.UUID

data class Mensaje(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val usuarioId: Int,
    val nombreUsuario: String,
    val asunto: String,
    val contenido: String,
    val respuesta: String? = null,
    val fechaCreacion: String,
    val leido: Boolean = false,
    val isPendingCreate: Boolean = false,
    val isPendingUpdate: Boolean = false
)
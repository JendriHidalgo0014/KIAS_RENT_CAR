package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "mensajes")
data class MensajeEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
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
package edu.ucne.kias_rent_car.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class MensajeEntity(
    @PrimaryKey(autoGenerate = true)
    val mensajeId: Int = 0,
    val remoteId: Int? = null,
    val usuarioId: Int,
    val nombreUsuario: String,
    val asunto: String,
    val contenido: String,
    val respuesta: String?,
    val fechaCreacion: String,
    val leido: Boolean = false,
    val isPendingCreate: Boolean = false,
    val isPendingRespuesta: Boolean = false
)
package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "ubicaciones")
data class UbicacionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val direccion: String? = null
)
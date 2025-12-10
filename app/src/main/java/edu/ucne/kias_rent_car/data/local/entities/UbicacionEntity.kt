package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ubicaciones")
data class UbicacionEntity(
    @PrimaryKey
    val ubicacionId: Int,
    val nombre: String,
    val direccion: String?,
    val activa: Boolean = true
)
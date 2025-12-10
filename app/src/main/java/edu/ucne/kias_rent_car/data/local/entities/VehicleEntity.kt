package edu.ucne.kias_rent_car.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey
    val id: String,
    val remoteId: Int,
    val modelo: String,
    val descripcion: String,
    val categoria: String,
    val asientos: Int,
    val transmision: String,
    val precioPorDia: Double,
    val imagenUrl: String,
    val disponible: Boolean,
    val isPendingCreate: Boolean = false,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
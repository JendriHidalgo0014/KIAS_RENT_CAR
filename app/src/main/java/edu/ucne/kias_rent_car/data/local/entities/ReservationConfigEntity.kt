package edu.ucne.kias_rent_car.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation_config")
data class ReservationConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val vehicleId: String,
    val lugarRecogida: String,
    val lugarDevolucion: String,
    val fechaRecogida: String,
    val fechaDevolucion: String,
    val horaRecogida: String,
    val horaDevolucion: String,
    val dias: Int,
    val subtotal: Double,
    val impuestos: Double,
    val total: Double,
    val ubicacionRecogidaId: Int,
    val ubicacionDevolucionId: Int
)
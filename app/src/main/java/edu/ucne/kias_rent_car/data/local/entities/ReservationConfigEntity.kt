package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reservation_config")
data class ReservationConfigEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
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
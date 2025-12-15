package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reservaciones")
data class ReservacionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val usuarioId: Int,
    val vehiculoId: Int,
    val fechaRecogida: String,
    val horaRecogida: String,
    val fechaDevolucion: String,
    val horaDevolucion: String,
    val ubicacionRecogidaId: Int,
    val ubicacionDevolucionId: Int,
    val estado: String,
    val subtotal: Double,
    val impuestos: Double,
    val total: Double,
    val codigoReserva: String,
    val fechaCreacion: String,
    val isPendingCreate: Boolean = false,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false,
    val vehiculoModelo: String = "",
    val vehiculoImagenUrl: String = "",
    val vehiculoPrecioPorDia: Double = 0.0,
    val ubicacionRecogidaNombre: String = "",
    val ubicacionDevolucionNombre: String = ""
)
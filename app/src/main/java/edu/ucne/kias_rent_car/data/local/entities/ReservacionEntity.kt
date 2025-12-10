package edu.ucne.kias_rent_car.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservaciones")
data class ReservacionEntity(
    @PrimaryKey
    val reservacionId: Int,
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
    val isPendingEstadoUpdate: Boolean = false,
    val vehiculoModelo: String = "",
    val vehiculoImagenUrl: String = "",
    val vehiculoPrecioPorDia: Double = 0.0,
    val ubicacionRecogidaNombre: String = "",
    val ubicacionDevolucionNombre: String = ""
)
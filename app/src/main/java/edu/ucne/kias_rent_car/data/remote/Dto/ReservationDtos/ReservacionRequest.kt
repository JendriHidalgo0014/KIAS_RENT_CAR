package edu.ucne.kias_rent_car.data.remote.dto

data class ReservacionRequest(
    val reservaId: Int,
    val vehiculoId: Int,
    val usuarioId: String,
    val fechaRecogida: String,
    val horaRecogida: String,
    val fechaDevolucion: String,
    val horaDevolucion: String,
    val lugarRecogida: String,
    val lugarDevolucion: String,
    val estado: String,
    val subtotal: Double,
    val impuestos: Double,
    val total: Double,
    val codigoReserva: String
)
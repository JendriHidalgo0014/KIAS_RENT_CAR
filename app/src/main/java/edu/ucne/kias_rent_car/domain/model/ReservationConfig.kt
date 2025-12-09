package edu.ucne.kias_rent_car.domain.model

data class ReservationConfig(
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
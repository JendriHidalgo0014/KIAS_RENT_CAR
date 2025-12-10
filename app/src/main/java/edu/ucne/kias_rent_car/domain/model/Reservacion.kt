package edu.ucne.kias_rent_car.domain.model

data class Reservacion(
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
    val usuario: Usuario? = null,
    val vehiculo: Vehicle? = null,
    val ubicacionRecogida: Ubicacion? = null,
    val ubicacionDevolucion: Ubicacion? = null
)
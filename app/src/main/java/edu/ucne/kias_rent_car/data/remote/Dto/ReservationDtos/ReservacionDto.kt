package edu.ucne.kias_rent_car.data.remote.dto

data class ReservacionDto(
    val reservacionId: Int,
    val usuarioId: Int,
    val vehiculoId: Int,
    val fechaRecogida: String? = null,
    val horaRecogida: String? = null,
    val fechaDevolucion: String? = null,
    val horaDevolucion: String? = null,
    val ubicacionRecogidaId: Int? = null,
    val ubicacionDevolucionId: Int? = null,
    val estado: String? = null,
    val subtotal: Double? = null,
    val impuestos: Double? = null,
    val total: Double? = null,
    val codigoReserva: String? = null,
    val fechaCreacion: String? = null,
    val usuario: UsuarioReservacionDto? = null,
    val vehiculo: VehiculoReservacionDto? = null,
    val ubicacionRecogida: UbicacionReservacionDto? = null,
    val ubicacionDevolucion: UbicacionReservacionDto? = null
)

data class UsuarioReservacionDto(
    val usuarioId: Int,
    val nombre: String,
    val email: String
)

data class VehiculoReservacionDto(
    val vehiculoId: Int,
    val modelo: String,
    val imagenUrl: String? = null,
    val precioPorDia: Double? = null
)

data class UbicacionReservacionDto(
    val ubicacionId: Int,
    val nombre: String
)
package edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos

import com.squareup.moshi.Json
import edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos.UbicacionReservacionDto
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.UsuarioReservacionDto
import edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos.VehiculoReservacionDto

data class ReservacionDto(
    @Json(name = "reservacionId")
    val reservacionId: Int,
    @Json(name = "usuarioId")
    val usuarioId: Int,
    @Json(name = "vehiculoId")
    val vehiculoId: Int,
    @Json(name = "fechaRecogida")
    val fechaRecogida: String,
    @Json(name = "horaRecogida")
    val horaRecogida: String,
    @Json(name = "fechaDevolucion")
    val fechaDevolucion: String,
    @Json(name = "horaDevolucion")
    val horaDevolucion: String,
    @Json(name = "ubicacionRecogidaId")
    val ubicacionRecogidaId: Int,
    @Json(name = "ubicacionDevolucionId")
    val ubicacionDevolucionId: Int,
    @Json(name = "estado")
    val estado: String,
    @Json(name = "subtotal")
    val subtotal: Double,
    @Json(name = "impuestos")
    val impuestos: Double,
    @Json(name = "total")
    val total: Double,
    @Json(name = "codigoReserva")
    val codigoReserva: String,
    @Json(name = "fechaCreacion")
    val fechaCreacion: String?,
    @Json(name = "usuario")
    val usuario: UsuarioReservacionDto? = null,
    @Json(name = "vehiculo")
    val vehiculo: VehiculoReservacionDto? = null,
    @Json(name = "ubicacionRecogida")
    val ubicacionRecogida: UbicacionReservacionDto? = null,
    @Json(name = "ubicacionDevolucion")
    val ubicacionDevolucion: UbicacionReservacionDto? = null
)
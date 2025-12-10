package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entity.ReservacionEntity
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.ReservacionDto
import edu.ucne.kias_rent_car.domain.model.*

object ReservacionMapper {

    fun ReservacionDto.toEntity(): ReservacionEntity {
        return ReservacionEntity(
            reservacionId = reservacionId,
            usuarioId = usuarioId,
            vehiculoId = vehiculoId,
            fechaRecogida = fechaRecogida,
            horaRecogida = horaRecogida,
            fechaDevolucion = fechaDevolucion,
            horaDevolucion = horaDevolucion,
            ubicacionRecogidaId = ubicacionRecogidaId,
            ubicacionDevolucionId = ubicacionDevolucionId,
            estado = estado,
            subtotal = subtotal,
            impuestos = impuestos,
            total = total,
            codigoReserva = codigoReserva,
            fechaCreacion = fechaCreacion ?: "",
            // Datos del vehículo para offline
            vehiculoModelo = vehiculo?.modelo ?: "",
            vehiculoImagenUrl = vehiculo?.imagenUrl ?: "",
            vehiculoPrecioPorDia = vehiculo?.precioPorDia ?: 0.0,
            // Datos de ubicación para offline
            ubicacionRecogidaNombre = ubicacionRecogida?.nombre ?: "",
            ubicacionDevolucionNombre = ubicacionDevolucion?.nombre ?: "",
            // Sync flags
            isPendingCreate = false,
            isPendingUpdate = false,
            isPendingEstadoUpdate = false
        )
    }

    fun ReservacionEntity.toDomain(): Reservacion {
        return Reservacion(
            reservacionId = reservacionId,
            usuarioId = usuarioId,
            vehiculoId = vehiculoId,
            fechaRecogida = fechaRecogida,
            horaRecogida = horaRecogida,
            fechaDevolucion = fechaDevolucion,
            horaDevolucion = horaDevolucion,
            ubicacionRecogidaId = ubicacionRecogidaId,
            ubicacionDevolucionId = ubicacionDevolucionId,
            estado = estado,
            subtotal = subtotal,
            impuestos = impuestos,
            total = total,
            codigoReserva = codigoReserva,
            fechaCreacion = fechaCreacion,
            vehiculo = if (vehiculoModelo.isNotBlank()) {
                Vehicle(
                    id = vehiculoId.toString(),
                    remoteId = vehiculoId,
                    modelo = vehiculoModelo,
                    descripcion = "",
                    categoria = VehicleCategory.SUV,
                    asientos = 5,
                    transmision = TransmisionType.AUTOMATIC,
                    precioPorDia = vehiculoPrecioPorDia,
                    imagenUrl = vehiculoImagenUrl,
                    disponible = true
                )
            } else null,
            ubicacionRecogida = if (ubicacionRecogidaNombre.isNotBlank()) {
                Ubicacion(
                    ubicacionId = ubicacionRecogidaId,
                    nombre = ubicacionRecogidaNombre,
                    direccion = null
                )
            } else null,
            ubicacionDevolucion = if (ubicacionDevolucionNombre.isNotBlank()) {
                Ubicacion(
                    ubicacionId = ubicacionDevolucionId,
                    nombre = ubicacionDevolucionNombre,
                    direccion = null
                )
            } else null
        )
    }

    fun ReservacionDto.toDomain(): Reservacion {
        return Reservacion(
            reservacionId = reservacionId,
            usuarioId = usuarioId,
            vehiculoId = vehiculoId,
            fechaRecogida = fechaRecogida,
            horaRecogida = horaRecogida,
            fechaDevolucion = fechaDevolucion,
            horaDevolucion = horaDevolucion,
            ubicacionRecogidaId = ubicacionRecogidaId,
            ubicacionDevolucionId = ubicacionDevolucionId,
            estado = estado,
            subtotal = subtotal,
            impuestos = impuestos,
            total = total,
            codigoReserva = codigoReserva,
            fechaCreacion = fechaCreacion ?: "",
            usuario = usuario?.let {
                Usuario(
                    id = it.usuarioId,
                    nombre = it.nombre,
                    email = it.email,
                    telefono = null,
                    rol = "Cliente"
                )
            },
            vehiculo = vehiculo?.let {
                Vehicle(
                    id = it.vehiculoId.toString(),
                    remoteId = it.vehiculoId,
                    modelo = it.modelo,
                    descripcion = "",
                    categoria = VehicleCategory.SUV,
                    asientos = 5,
                    transmision = TransmisionType.AUTOMATIC,
                    precioPorDia = it.precioPorDia ?: 0.0,
                    imagenUrl = it.imagenUrl ?: "",
                    disponible = true
                )
            },
            ubicacionRecogida = ubicacionRecogida?.let {
                Ubicacion(
                    ubicacionId = it.ubicacionId,
                    nombre = it.nombre,
                    direccion = null
                )
            },
            ubicacionDevolucion = ubicacionDevolucion?.let {
                Ubicacion(
                    ubicacionId = it.ubicacionId,
                    nombre = it.nombre,
                    direccion = null
                )
            }
        )
    }

    fun List<ReservacionDto>.toEntityList() = map { it.toEntity() }
    fun List<ReservacionEntity>.toDomainList() = map { it.toDomain() }
}
package edu.ucne.kias_rent_car.data.mappers


import edu.ucne.kias_rent_car.data.local.entities.ReservacionEntity
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionDto
import edu.ucne.kias_rent_car.domain.model.Reservacion
import java.util.UUID

object ReservacionMapper {

    fun ReservacionEntity.toDomain(): Reservacion = Reservacion(
        id = id,
        remoteId = remoteId,
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
        usuario = null,
        vehiculo = null,
        ubicacionRecogida = null,
        ubicacionDevolucion = null,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )

    fun Reservacion.toEntity(): ReservacionEntity = ReservacionEntity(
        id = id,
        remoteId = remoteId,
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
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete,
        vehiculoModelo = vehiculo?.modelo ?: "",
        vehiculoImagenUrl = vehiculo?.imagenUrl ?: "",
        vehiculoPrecioPorDia = vehiculo?.precioPorDia ?: 0.0,
        ubicacionRecogidaNombre = ubicacionRecogida?.nombre ?: "",
        ubicacionDevolucionNombre = ubicacionDevolucion?.nombre ?: ""
    )

    fun ReservacionDto.toDomain(): Reservacion = Reservacion(
        id = UUID.randomUUID().toString(),
        remoteId = reservacionId,
        usuarioId = usuarioId,
        vehiculoId = vehiculoId,
        fechaRecogida = fechaRecogida ?: "",
        horaRecogida = horaRecogida ?: "10:00",
        fechaDevolucion = fechaDevolucion ?: "",
        horaDevolucion = horaDevolucion ?: "10:00",
        ubicacionRecogidaId = ubicacionRecogidaId ?: 1,
        ubicacionDevolucionId = ubicacionDevolucionId ?: 1,
        estado = estado ?: "Pendiente",
        subtotal = subtotal ?: 0.0,
        impuestos = impuestos ?: 0.0,
        total = total ?: 0.0,
        codigoReserva = codigoReserva ?: "",
        fechaCreacion = fechaCreacion ?: "",
        usuario = null,
        vehiculo = null,
        ubicacionRecogida = null,
        ubicacionDevolucion = null,
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false
    )

    fun ReservacionDto.toEntity(): ReservacionEntity = ReservacionEntity(
        id = UUID.randomUUID().toString(),
        remoteId = reservacionId,
        usuarioId = usuarioId,
        vehiculoId = vehiculoId,
        fechaRecogida = fechaRecogida ?: "",
        horaRecogida = horaRecogida ?: "10:00",
        fechaDevolucion = fechaDevolucion ?: "",
        horaDevolucion = horaDevolucion ?: "10:00",
        ubicacionRecogidaId = ubicacionRecogidaId ?: 1,
        ubicacionDevolucionId = ubicacionDevolucionId ?: 1,
        estado = estado ?: "Pendiente",
        subtotal = subtotal ?: 0.0,
        impuestos = impuestos ?: 0.0,
        total = total ?: 0.0,
        codigoReserva = codigoReserva ?: "",
        fechaCreacion = fechaCreacion ?: "",
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false,
        vehiculoModelo = vehiculo?.modelo ?: "",
        vehiculoImagenUrl = vehiculo?.imagenUrl ?: "",
        vehiculoPrecioPorDia = vehiculo?.precioPorDia ?: 0.0,
        ubicacionRecogidaNombre = ubicacionRecogida?.nombre ?: "",
        ubicacionDevolucionNombre = ubicacionDevolucion?.nombre ?: ""
    )

    fun List<ReservacionEntity>.toDomainList(): List<Reservacion> = map { it.toDomain() }
    fun List<ReservacionDto>.toEntityList(): List<ReservacionEntity> = map { it.toEntity() }
    fun List<ReservacionDto>.toDomainList(): List<Reservacion> = map { it.toDomain() }
}
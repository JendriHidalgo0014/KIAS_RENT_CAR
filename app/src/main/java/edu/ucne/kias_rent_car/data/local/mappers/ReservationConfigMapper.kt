package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entities.ReservationConfigEntity
import edu.ucne.kias_rent_car.domain.model.ReservationConfig

object ReservationConfigMapper {

    fun ReservationConfigEntity.toDomain(): ReservationConfig = ReservationConfig(
        id = id,
        vehicleId = vehicleId,
        lugarRecogida = lugarRecogida,
        lugarDevolucion = lugarDevolucion,
        fechaRecogida = fechaRecogida,
        fechaDevolucion = fechaDevolucion,
        horaRecogida = horaRecogida,
        horaDevolucion = horaDevolucion,
        dias = dias,
        subtotal = subtotal,
        impuestos = impuestos,
        total = total,
        ubicacionRecogidaId = ubicacionRecogidaId,
        ubicacionDevolucionId = ubicacionDevolucionId
    )

    fun ReservationConfig.toEntity(): ReservationConfigEntity = ReservationConfigEntity(
        id = id,
        vehicleId = vehicleId,
        lugarRecogida = lugarRecogida,
        lugarDevolucion = lugarDevolucion,
        fechaRecogida = fechaRecogida,
        fechaDevolucion = fechaDevolucion,
        horaRecogida = horaRecogida,
        horaDevolucion = horaDevolucion,
        dias = dias,
        subtotal = subtotal,
        impuestos = impuestos,
        total = total,
        ubicacionRecogidaId = ubicacionRecogidaId,
        ubicacionDevolucionId = ubicacionDevolucionId
    )
}
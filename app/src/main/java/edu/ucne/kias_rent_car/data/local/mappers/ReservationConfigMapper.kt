package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entity.ReservationConfigEntity
import edu.ucne.kias_rent_car.domain.model.ReservationConfig

object ReservationConfigMapper {
    fun ReservationConfigEntity.toDomain(): ReservationConfig {
        return ReservationConfig(
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
    fun ReservationConfig.toEntity(): ReservationConfigEntity {
        return ReservationConfigEntity(
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
}
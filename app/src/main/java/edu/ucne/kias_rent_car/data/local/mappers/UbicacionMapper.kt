package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entities.UbicacionEntity
import edu.ucne.kias_rent_car.data.remote.dto.UbicacionDto
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import java.util.UUID

object UbicacionMapper {

    fun UbicacionEntity.toDomain(): Ubicacion = Ubicacion(
        id = id,
        remoteId = remoteId,
        nombre = nombre,
        direccion = direccion
    )

    fun Ubicacion.toEntity(): UbicacionEntity = UbicacionEntity(
        id = id,
        remoteId = remoteId,
        nombre = nombre,
        direccion = direccion
    )

    fun UbicacionDto.toDomain(): Ubicacion = Ubicacion(
        id = UUID.randomUUID().toString(),
        remoteId = ubicacionId,
        nombre = nombre,
        direccion = direccion
    )

    fun UbicacionDto.toEntity(): UbicacionEntity = UbicacionEntity(
        id = UUID.randomUUID().toString(),
        remoteId = ubicacionId,
        nombre = nombre,
        direccion = direccion
    )

    fun List<UbicacionEntity>.toDomainList(): List<Ubicacion> = map { it.toDomain() }
    fun List<UbicacionDto>.toEntityList(): List<UbicacionEntity> = map { it.toEntity() }
    fun List<UbicacionDto>.toDomainList(): List<Ubicacion> = map { it.toDomain() }
}
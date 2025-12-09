package edu.ucne.kias_rent_car.data.local.mappers

import edu.ucne.kias_rent_car.data.local.entities.UbicacionEntity
import edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos.UbicacionDto
import edu.ucne.kias_rent_car.domain.model.Ubicacion

object UbicacionMapper {
    // DTO -> Entity
    fun UbicacionDto.toEntity(): UbicacionEntity {
        return UbicacionEntity(
            ubicacionId = this.ubicacionId,
            nombre = this.nombre,
            direccion = this.direccion,
            activa = this.activa
        )
    }
    // Entity -> Domain
    fun UbicacionEntity.toDomain(): Ubicacion {
        return Ubicacion(
            ubicacionId = this.ubicacionId,
            nombre = this.nombre,
            direccion = this.direccion
        )
    }
    // DTO -> Domain
    fun UbicacionDto.toDomain(): Ubicacion {
        return Ubicacion(
            ubicacionId = this.ubicacionId,
            nombre = this.nombre,
            direccion = this.direccion
        )
    }
    // Lists
    fun List<UbicacionDto>.toEntityList(): List<UbicacionEntity> {
        return this.map { it.toEntity() }
    }
    fun List<UbicacionEntity>.toDomainList(): List<Ubicacion> {
        return this.map { it.toDomain() }
    }
}
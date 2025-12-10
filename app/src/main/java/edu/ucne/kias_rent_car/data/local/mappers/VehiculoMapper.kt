package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entity.VehicleEntity
import edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos.VehiculoDto
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.model.TransmisionType

object VehiculoMapper {
    fun VehiculoDto.toEntity(): VehicleEntity {
        return VehicleEntity(
            id = vehiculoId.toString(),
            remoteId = vehiculoId,
            modelo = modelo,
            descripcion = descripcion ?: "",
            categoria = categoria,
            asientos = asientos,
            transmision = transmision,
            precioPorDia = precioPorDia,
            imagenUrl = imagenUrl ?: "",
            disponible = disponible
        )
    }

    fun VehicleEntity.toDomain(): Vehicle {
        return Vehicle(
            id = id,
            remoteId = remoteId,
            modelo = modelo,
            descripcion = descripcion,
            categoria = try {
                VehicleCategory.valueOf(categoria.uppercase())
            } catch (e: Exception) {
                VehicleCategory.SUV
            },
            asientos = asientos,
            transmision = try {
                TransmisionType.valueOf(transmision.uppercase())
            } catch (e: Exception) {
                TransmisionType.AUTOMATIC
            },
            precioPorDia = precioPorDia,
            imagenUrl = imagenUrl,
            disponible = disponible
        )
    }

    fun VehiculoDto.toDomain(): Vehicle {
        return Vehicle(
            id = vehiculoId.toString(),
            remoteId = vehiculoId,
            modelo = modelo,
            descripcion = descripcion ?: "",
            categoria = try {
                VehicleCategory.valueOf(categoria.uppercase())
            } catch (e: Exception) {
                VehicleCategory.SUV
            },
            asientos = asientos,
            transmision = try {
                TransmisionType.valueOf(transmision.uppercase())
            } catch (e: Exception) {
                TransmisionType.AUTOMATIC
            },
            precioPorDia = precioPorDia,
            imagenUrl = imagenUrl ?: "",
            disponible = disponible
        )
    }
    fun List<VehiculoDto>.toEntityList() = map { it.toEntity() }
    fun List<VehicleEntity>.toDomainList() = map { it.toDomain() }
}
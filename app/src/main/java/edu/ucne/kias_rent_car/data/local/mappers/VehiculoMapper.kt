package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entities.VehicleEntity
import edu.ucne.kias_rent_car.data.remote.dto.VehiculoDto
import edu.ucne.kias_rent_car.domain.model.TransmisionType
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import java.util.UUID

object VehiculoMapper {

    fun VehicleEntity.toDomain(): Vehicle = Vehicle(
        id = id,
        remoteId = remoteId,
        modelo = modelo,
        descripcion = descripcion,
        categoria = parseCategory(categoria),
        asientos = asientos,
        transmision = parseTransmision(transmision),
        precioPorDia = precioPorDia,
        imagenUrl = imagenUrl,
        disponible = disponible,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )

    fun Vehicle.toEntity(): VehicleEntity = VehicleEntity(
        id = id,
        remoteId = remoteId,
        modelo = modelo,
        descripcion = descripcion,
        categoria = categoria.name,
        asientos = asientos,
        transmision = transmision.name,
        precioPorDia = precioPorDia,
        imagenUrl = imagenUrl,
        disponible = disponible,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )

    fun VehiculoDto.toDomain(): Vehicle = Vehicle(
        id = UUID.randomUUID().toString(),
        remoteId = vehiculoId,
        modelo = modelo,
        descripcion = descripcion ?: "",
        categoria = parseCategory(categoria ?: "SUV"),
        asientos = asientos ?: 5,
        transmision = parseTransmision(transmision ?: "AUTOMATIC"),
        precioPorDia = precioPorDia,
        imagenUrl = imagenUrl ?: "",
        disponible = disponible ?: true,
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false
    )

    fun VehiculoDto.toEntity(): VehicleEntity = VehicleEntity(
        id = UUID.randomUUID().toString(),
        remoteId = vehiculoId,
        modelo = modelo,
        descripcion = descripcion ?: "",
        categoria = categoria ?: "SUV",
        asientos = asientos ?: 5,
        transmision = transmision ?: "AUTOMATIC",
        precioPorDia = precioPorDia,
        imagenUrl = imagenUrl ?: "",
        disponible = disponible ?: true,
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false
    )

    private fun parseCategory(value: String): VehicleCategory {
        return VehicleCategory.entries.find {
            it.name.equals(value, ignoreCase = true)
        } ?: VehicleCategory.SUV
    }

    private fun parseTransmision(value: String): TransmisionType {
        return TransmisionType.entries.find {
            it.name.equals(value, ignoreCase = true)
        } ?: TransmisionType.AUTOMATIC
    }

    fun List<VehicleEntity>.toDomainList(): List<Vehicle> = map { it.toDomain() }
    fun List<VehiculoDto>.toEntityList(): List<VehicleEntity> = map { it.toEntity() }
    fun List<VehiculoDto>.toDomainList(): List<Vehicle> = map { it.toDomain() }
}
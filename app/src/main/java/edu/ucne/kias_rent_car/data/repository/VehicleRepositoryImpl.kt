package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.mappers.VehiculoMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.VehiculoMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.VehiculoMapper.toEntityList
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.VehiculoRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val remoteDataSource: VehiculoRemoteDataSource,
    private val vehicleDao: VehicleDao
) : VehicleRepository {
    override fun observeAvailableVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.observeAllVehicles().map { entities ->
            entities.toDomainList()
        }
    }

    override fun observeVehiclesByCategory(category: VehicleCategory): Flow<List<Vehicle>> {
        return if (category == VehicleCategory.ALL) {
            observeAvailableVehicles()
        } else {
            vehicleDao.observeVehiclesByCategory(category.name).map { entities ->
                entities.toDomainList()
            }
        }
    }

    override fun searchVehicles(query: String): Flow<List<Vehicle>> {
        return vehicleDao.searchVehicles("%$query%").map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getVehicle(id: String): Vehicle? {
        val local = vehicleDao.getVehicleById(id)
        if (local != null) {
            return local.toDomain()
        }

        val remoteId = id.toIntOrNull() ?: return null
        val remoto = remoteDataSource.getVehiculoById(remoteId)
        return remoto?.toDomain()
    }

    override suspend fun refreshVehicles(): Resource<Unit> {
        return try {
            val remotos = remoteDataSource.getAllVehiculos()
            if (remotos != null) {
                vehicleDao.deleteAll()
                vehicleDao.insertVehicles(remotos.toEntityList())
                Resource.Success(Unit)
            } else {
                Resource.Error("Error al obtener vehículos")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
        }
    }

    suspend fun createVehicle(
        modelo: String,
        descripcion: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Vehicle> {
        return try {
            val response = remoteDataSource.createVehiculo(
                modelo = modelo,
                descripcion = descripcion,
                precioPorDia = precioPorDia,
                imagenUrl = imagenUrl
            )
            if (response != null) {
                Resource.Success(response.toDomain())
            } else {
                Resource.Error("Error al crear vehículo")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}")
        }
    }

    suspend fun updateVehicle(
        id: String,
        modelo: String,
        descripcion: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Unit> {
        return try {
            val remoteId = id.toIntOrNull() ?: return Resource.Error("ID inválido")
            val success = remoteDataSource.updateVehiculo(
                id = remoteId,
                modelo = modelo,
                descripcion = descripcion,
                precioPorDia = precioPorDia,
                imagenUrl = imagenUrl
            )
            if (success) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error al actualizar")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}")
        }
    }

    suspend fun deleteVehicle(id: String): Resource<Unit> {
        return try {
            val remoteId = id.toIntOrNull() ?: return Resource.Error("ID inválido")
            val success = remoteDataSource.deleteVehiculo(remoteId)
            if (success) {
                vehicleDao.deleteById(id)
                Resource.Success(Unit)
            } else {
                Resource.Error("Error al eliminar")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}")
        }
    }
}
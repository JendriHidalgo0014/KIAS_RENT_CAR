package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.mappers.VehiculoMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.VehiculoMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.VehiculoMapper.toEntity
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
    private val localDataSource: VehicleDao,
    private val remoteDataSource: VehiculoRemoteDataSource
) : VehicleRepository {

    override fun observeAvailableVehicles(): Flow<List<Vehicle>> {
        return localDataSource.observeAllVehicles().map { it.toDomainList() }
    }

    override fun observeVehiclesByCategory(category: VehicleCategory): Flow<List<Vehicle>> {
        return if (category == VehicleCategory.ALL) {
            observeAvailableVehicles()
        } else {
            localDataSource.observeVehiclesByCategory(category.name).map { it.toDomainList() }
        }
    }

    override fun searchVehicles(query: String): Flow<List<Vehicle>> {
        return localDataSource.searchVehicles("%$query%").map { it.toDomainList() }
    }

    override suspend fun getVehicle(id: String): Resource<Vehicle> {
        val local = localDataSource.getById(id)
        if (local != null) {
            return Resource.Success(local.toDomain())
        }

        val remoteId = id.toIntOrNull()
        if (remoteId != null) {
            val byRemote = localDataSource.getByRemoteId(remoteId)
            if (byRemote != null) {
                return Resource.Success(byRemote.toDomain())
            }

            return when (val result = remoteDataSource.getVehiculoById(remoteId)) {
                is Resource.Success -> Resource.Success(result.data.toDomain())
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading()
            }
        }
        return Resource.Error("ID inválido")
    }

    override suspend fun refreshVehicles(): Resource<Unit> {
        return when (val result = remoteDataSource.getAllVehiculos()) {
            is Resource.Success -> {
                localDataSource.deleteAll()
                localDataSource.insertVehicles(result.data.toEntityList())
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun createVehicle(
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Vehicle> {
        return when (val result = remoteDataSource.createVehiculo(
            modelo, descripcion, categoria, asientos, transmision, precioPorDia, imagenUrl
        )) {
            is Resource.Success -> {
                localDataSource.insertVehicle(result.data.toEntity())
                Resource.Success(result.data.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun updateVehicle(
        id: String,
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Unit> {
        val vehicle = localDataSource.getById(id)
        val remoteId = vehicle?.remoteId ?: id.toIntOrNull()

        if (remoteId == null) {
            return Resource.Error("ID inválido")
        }

        return when (val result = remoteDataSource.updateVehiculo(
            remoteId, modelo, descripcion, categoria, asientos, transmision, precioPorDia, imagenUrl
        )) {
            is Resource.Success -> {
                refreshVehicles()
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun deleteVehicle(id: String): Resource<Unit> {
        val vehicle = localDataSource.getById(id)
        val remoteId = vehicle?.remoteId ?: id.toIntOrNull()

        if (remoteId == null) {
            return Resource.Error("ID inválido")
        }

        return when (val result = remoteDataSource.deleteVehiculo(remoteId)) {
            is Resource.Success -> {
                localDataSource.deleteById(id)
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }
}
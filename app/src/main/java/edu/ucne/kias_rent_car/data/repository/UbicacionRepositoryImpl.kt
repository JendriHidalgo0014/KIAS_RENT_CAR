package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.mappers.UbicacionMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.UbicacionMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.UbicacionMapper.toEntityList
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.UbicacionRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.domain.repository.UbicacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UbicacionRepositoryImpl @Inject constructor(
    private val remoteDataSource: UbicacionRemoteDataSource,
    private val localDataSource: UbicacionDao
) : UbicacionRepository {

    override suspend fun getUbicaciones(): List<Ubicacion> {
        return try {
            when (val result = remoteDataSource.getUbicaciones()) {
                is Resource.Success -> {
                    localDataSource.insertUbicaciones(result.data.toEntityList())
                    localDataSource.getUbicaciones().toDomainList()
                }
                else -> localDataSource.getUbicaciones().toDomainList()
            }
        } catch (e: Exception) {
            localDataSource.getUbicaciones().toDomainList()
        }
    }

    override fun observeUbicaciones(): Flow<List<Ubicacion>> {
        return localDataSource.observeUbicaciones().map { it.toDomainList() }
    }

    override suspend fun getUbicacionById(id: String): Ubicacion? {
        val local = localDataSource.getById(id)
        if (local != null) {
            return local.toDomain()
        }

        val remoteId = id.toIntOrNull()
        if (remoteId != null) {
            val byRemote = localDataSource.getByRemoteId(remoteId)
            if (byRemote != null) {
                return byRemote.toDomain()
            }

            return when (val result = remoteDataSource.getUbicacionById(remoteId)) {
                is Resource.Success -> result.data.toDomain()
                else -> null
            }
        }
        return null
    }

    override suspend fun refreshUbicaciones() {
        when (val result = remoteDataSource.getUbicaciones()) {
            is Resource.Success -> {
                localDataSource.deleteAll()
                localDataSource.insertUbicaciones(result.data.toEntityList())
            }
            else -> Unit
        }
    }
}
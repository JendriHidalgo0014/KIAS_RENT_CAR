package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.mappers.UbicacionMapper.toDomain
import edu.ucne.kias_rent_car.data.local.mappers.UbicacionMapper.toDomainList
import edu.ucne.kias_rent_car.data.local.mappers.UbicacionMapper.toEntityList
import edu.ucne.kias_rent_car.data.remote.RemoteDataSource.UbicacionRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.domain.repository.UbicacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UbicacionRepositoryImpl @Inject constructor(
    private val remoteDataSource: UbicacionRemoteDataSource,
    private val ubicacionDao: UbicacionDao,
 ) : UbicacionRepository {
    override suspend fun getUbicaciones(): List<Ubicacion> {
        try {
            val remotas = remoteDataSource.getUbicaciones()
            if (remotas != null && remotas.isNotEmpty()) {
                ubicacionDao.insertUbicaciones(remotas.toEntityList())
            }
        } catch (e: Exception) {
        }
        return ubicacionDao.getUbicaciones().toDomainList()
    }

    override fun observeUbicaciones(): Flow<List<Ubicacion>> {
        return ubicacionDao.observeUbicaciones().map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getUbicacionById(id: Int): Ubicacion? {
        val local = ubicacionDao.getUbicacionById(id)
        if (local != null) {
            return local.toDomain()
        }
        val remoto = remoteDataSource.getUbicacionById(id)
        return remoto?.toDomain()
    }

    override suspend fun refreshUbicaciones() {
        try {
            val remotas = remoteDataSource.getUbicaciones()
            if (remotas != null) {
                ubicacionDao.deleteAll()
                ubicacionDao.insertUbicaciones(remotas.toEntityList())
            }
        } catch (e: Exception) {
        }
    }
}
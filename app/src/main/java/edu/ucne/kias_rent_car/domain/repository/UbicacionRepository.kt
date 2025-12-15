package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.domain.model.Ubicacion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun getUbicaciones(): List<Ubicacion>
    fun observeUbicaciones(): Flow<List<Ubicacion>>
    suspend fun getUbicacionById(id: String): Ubicacion?
    suspend fun refreshUbicaciones()
}
package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    suspend fun observeAvailableVehicles(): Flow<List<Vehicle>>
    suspend fun observeVehiclesByCategory(category: VehicleCategory): Flow<List<Vehicle>>
    suspend fun searchVehicles(query: String): Flow<List<Vehicle>>
    suspend fun getVehicle(id: String): Vehicle?
    suspend fun refreshVehicles(): Resource<Unit>
}
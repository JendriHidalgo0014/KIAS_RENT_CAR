package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun observeAvailableVehicles(): Flow<List<Vehicle>>
    fun observeVehiclesByCategory(category: VehicleCategory): Flow<List<Vehicle>>
    fun searchVehicles(query: String): Flow<List<Vehicle>>
    suspend fun getVehicle(id: String): Resource<Vehicle>
    suspend fun refreshVehicles(): Resource<Unit>
    suspend fun createVehicle(
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Vehicle>
    suspend fun updateVehicle(
        id: String,
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Unit>
    suspend fun deleteVehicle(id: String): Resource<Unit>
}
package edu.ucne.kias_rent_car.data.remote.RemoteDataSource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos.UbicacionDto
import javax.inject.Inject

class UbicacionRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getUbicaciones(): List<UbicacionDto>? {
        return try {
            val response = apiService.getUbicaciones()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getUbicacionById(id: Int): UbicacionDto? {
        return try {
            val response = apiService.getUbicacionById(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
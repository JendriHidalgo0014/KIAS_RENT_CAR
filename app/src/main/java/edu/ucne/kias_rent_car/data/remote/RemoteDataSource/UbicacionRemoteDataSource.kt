package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.dto.UbicacionDto
import javax.inject.Inject

class UbicacionRemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun getUbicaciones(): Resource<List<UbicacionDto>> {
        return try {
            val response = api.getUbicaciones()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getUbicacionById(id: Int): Resource<UbicacionDto> {
        return try {
            val response = api.getUbicacionById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Ubicación no encontrada")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}
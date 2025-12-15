package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.dto.VehiculoDto
import edu.ucne.kias_rent_car.data.remote.dto.VehiculoRequest
import javax.inject.Inject

class VehiculoRemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun getVehiculosDisponibles(): Resource<List<VehiculoDto>> {
        return try {
            val response = api.getVehiculosDisponibles()
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

    suspend fun getAllVehiculos(): Resource<List<VehiculoDto>> {
        return try {
            val response = api.getAllVehiculos()
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

    suspend fun getVehiculoById(id: Int): Resource<VehiculoDto> {
        return try {
            val response = api.getVehiculoById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Vehículo no encontrado")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun createVehiculo(
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<VehiculoDto> {
        return try {
            val request = VehiculoRequest(
                modelo = modelo,
                descripcion = descripcion,
                categoria = categoria,
                asientos = asientos,
                transmision = transmision,
                precioPorDia = precioPorDia,
                imagenUrl = imagenUrl
            )
            val response = api.createVehiculo(request)
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

    suspend fun updateVehiculo(
        id: Int,
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Unit> {
        return try {
            val request = VehiculoRequest(
                modelo = modelo,
                descripcion = descripcion,
                categoria = categoria,
                asientos = asientos,
                transmision = transmision,
                precioPorDia = precioPorDia,
                imagenUrl = imagenUrl
            )
            val response = api.updateVehiculo(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun deleteVehiculo(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteVehiculo(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}
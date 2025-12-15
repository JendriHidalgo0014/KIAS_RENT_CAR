package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.dto.EstadoRequest
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionDto
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionRequest
import edu.ucne.kias_rent_car.data.remote.dto.UpdateDatosRequest

import javax.inject.Inject

class ReservacionRemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun getReservaciones(): Resource<List<ReservacionDto>> {
        return try {
            val response = api.getReservaciones()
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

    suspend fun getReservacionesByUsuario(usuarioId: Int): Resource<List<ReservacionDto>> {
        return try {
            val response = api.getReservacionesByUsuario(usuarioId)
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

    suspend fun getReservacionById(id: Int): Resource<ReservacionDto> {
        return try {
            val response = api.getReservacionById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Reservación no encontrada")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun createReservacion(request: ReservacionRequest): Resource<ReservacionDto> {
        return try {
            val response = api.createReservacion(request)
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

    suspend fun updateEstado(reservacionId: Int, estado: String): Resource<Unit> {
        return try {
            val response = api.updateEstadoReservacion(reservacionId, EstadoRequest(estado))
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun updateReservacion(
        reservacionId: Int,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Resource<Unit> {
        return try {
            val request = UpdateDatosRequest(
                ubicacionRecogidaId = ubicacionRecogidaId,
                ubicacionDevolucionId = ubicacionDevolucionId,
                fechaRecogida = fechaRecogida,
                horaRecogida = horaRecogida,
                fechaDevolucion = fechaDevolucion,
                horaDevolucion = horaDevolucion
            )
            val response = api.updateReservacionDatos(reservacionId, request)
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
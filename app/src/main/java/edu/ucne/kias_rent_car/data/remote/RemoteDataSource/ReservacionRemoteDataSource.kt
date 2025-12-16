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
    private val errorRed = "Error de red"
    private val errorRespuestaVacia = "Respuesta vacía del servidor"
    private val errorReservacionNoEncontrada = "Reservación no encontrada"

    suspend fun getReservaciones(): Resource<List<ReservacionDto>> {
        return try {
            val response = api.getReservaciones()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error(errorRespuestaVacia)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: errorRed)
        }
    }

    suspend fun getReservacionesByUsuario(usuarioId: Int): Resource<List<ReservacionDto>> {
        return try {
            val response = api.getReservacionesByUsuario(usuarioId)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error(errorRespuestaVacia)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: errorRed)
        }
    }

    suspend fun getReservacionById(id: Int): Resource<ReservacionDto> {
        return try {
            val response = api.getReservacionById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error(errorReservacionNoEncontrada)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: errorRed)
        }
    }

    suspend fun createReservacion(request: ReservacionRequest): Resource<ReservacionDto> {
        return try {
            val response = api.createReservacion(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error(errorRespuestaVacia)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: errorRed)
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
            Resource.Error(e.localizedMessage ?: errorRed)
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
            Resource.Error(e.localizedMessage ?: errorRed)
        }
    }
}
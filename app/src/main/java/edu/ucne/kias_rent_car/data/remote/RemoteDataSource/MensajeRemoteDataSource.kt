package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.dto.MensajeDto
import edu.ucne.kias_rent_car.data.remote.dto.MensajeRequest
import edu.ucne.kias_rent_car.data.remote.dto.RespuestaRequest
import javax.inject.Inject

class MensajeRemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun getMensajes(): Resource<List<MensajeDto>> {
        return try {
            val response = api.getMensajes()
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

    suspend fun getMensajeById(id: Int): Resource<MensajeDto> {
        return try {
            val response = api.getMensajeById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Mensaje no encontrado")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getMensajesByUsuario(usuarioId: Int): Resource<List<MensajeDto>> {
        return try {
            val response = api.getMensajesByUsuario(usuarioId)
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

    suspend fun sendMensaje(usuarioId: Int, asunto: String, contenido: String): Resource<MensajeDto> {
        return try {
            val request = MensajeRequest(
                usuarioId = usuarioId,
                asunto = asunto,
                contenido = contenido
            )
            val response = api.createMensaje(request)
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

    suspend fun responderMensaje(mensajeId: Int, respuesta: String): Resource<Unit> {
        return try {
            val response = api.responderMensaje(mensajeId, RespuestaRequest(respuesta))
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
package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.MensajeDto
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.MensajeRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.RespuestaRequest
import javax.inject.Inject

class MensajeRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMensajes(): List<MensajeDto>? {
        return try {
            val response = apiService.getMensajes()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getMensajeById(id: Int): MensajeDto? {
        return try {
            val response = apiService.getMensajeById(id)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getMensajesByUsuario(usuarioId: Int): List<MensajeDto>? {
        return try {
            val response = apiService.getMensajesByUsuario(usuarioId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun sendMensaje(usuarioId: Int, asunto: String, contenido: String): MensajeDto? {
        return try {
            val request = MensajeRequest(
                usuarioId = usuarioId,
                asunto = asunto,
                contenido = contenido
            )
            val response = apiService.createMensaje(request)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun responderMensaje(mensajeId: Int, respuesta: String): Boolean {
        return try {
            val response = apiService.responderMensaje(mensajeId, RespuestaRequest(respuesta))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Mensaje

interface MensajeRepository {
    suspend fun getMensajes(): Resource<List<Mensaje>>
    suspend fun getMensajeById(id: String): Resource<Mensaje>
    suspend fun getMensajesByUsuario(usuarioId: Int): Resource<List<Mensaje>>
    suspend fun createMensajeLocal(asunto: String, contenido: String): Resource<Mensaje>
    suspend fun sendMensaje(asunto: String, contenido: String): Resource<Mensaje>
    suspend fun responderMensaje(mensajeId: String, respuesta: String): Resource<Unit>
    suspend fun postPendingMensajes(): Resource<Unit>
}
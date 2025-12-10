package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.domain.model.Mensaje

interface MensajeRepository {
    suspend fun getMensajes(): List<Mensaje>
    suspend fun getMensajeById(id: Int): Mensaje?
    suspend fun sendMensaje(asunto: String, contenido: String): Mensaje?
    suspend fun responderMensaje(mensajeId: Int, respuesta: String)
    suspend fun getMensajesByUsuario(usuarioId: Int): List<Mensaje>
}
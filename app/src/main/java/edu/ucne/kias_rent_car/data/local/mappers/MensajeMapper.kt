package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entities.MensajeEntity
import edu.ucne.kias_rent_car.data.remote.dto.MensajeDto
import edu.ucne.kias_rent_car.domain.model.Mensaje
import java.util.UUID

object MensajeMapper {

    fun MensajeEntity.toDomain(): Mensaje = Mensaje(
        id = id,
        remoteId = remoteId,
        usuarioId = usuarioId,
        nombreUsuario = nombreUsuario,
        asunto = asunto,
        contenido = contenido,
        respuesta = respuesta,
        fechaCreacion = fechaCreacion,
        leido = leido,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate
    )

    fun Mensaje.toEntity(): MensajeEntity = MensajeEntity(
        id = id,
        remoteId = remoteId,
        usuarioId = usuarioId,
        nombreUsuario = nombreUsuario,
        asunto = asunto,
        contenido = contenido,
        respuesta = respuesta,
        fechaCreacion = fechaCreacion,
        leido = leido,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate
    )

    fun MensajeDto.toDomain(): Mensaje = Mensaje(
        id = UUID.randomUUID().toString(),
        remoteId = mensajeId,
        usuarioId = usuarioId,
        nombreUsuario = nombreUsuario ?: "Usuario",
        asunto = asunto,
        contenido = contenido,
        respuesta = respuesta,
        fechaCreacion = fechaCreacion ?: "",
        leido = leido ?: false,
        isPendingCreate = false,
        isPendingUpdate = false
    )

    fun MensajeDto.toEntity(): MensajeEntity = MensajeEntity(
        id = UUID.randomUUID().toString(),
        remoteId = mensajeId,
        usuarioId = usuarioId,
        nombreUsuario = nombreUsuario ?: "Usuario",
        asunto = asunto,
        contenido = contenido,
        respuesta = respuesta,
        fechaCreacion = fechaCreacion ?: "",
        leido = leido ?: false,
        isPendingCreate = false,
        isPendingUpdate = false
    )
    fun List<MensajeEntity>.toDomainList(): List<Mensaje> = map { it.toDomain() }
    fun List<MensajeDto>.toEntityList(): List<MensajeEntity> = map { it.toEntity() }
    fun List<MensajeDto>.toDomainList(): List<Mensaje> = map { it.toDomain() }
}
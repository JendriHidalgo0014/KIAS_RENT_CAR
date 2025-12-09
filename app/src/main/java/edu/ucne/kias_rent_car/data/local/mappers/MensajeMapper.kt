package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entity.MensajeEntity
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.MensajeDto
import edu.ucne.kias_rent_car.domain.model.Mensaje

object MensajeMapper {
    fun MensajeDto.toEntity(): MensajeEntity {
        return MensajeEntity(
            mensajeId = mensajeId,
            remoteId = mensajeId,  // El ID remoto es el mismo
            usuarioId = usuarioId,
            nombreUsuario = nombreUsuario,
            asunto = asunto,
            contenido = contenido,
            respuesta = respuesta,
            fechaCreacion = fechaCreacion ?: "",
            leido = leido,
            isPendingCreate = false,
            isPendingRespuesta = false
        )
    }

    fun MensajeEntity.toDomain(): Mensaje {
        return Mensaje(
            mensajeId = mensajeId,
            usuarioId = usuarioId,
            nombreUsuario = nombreUsuario,
            asunto = asunto,
            contenido = contenido,
            respuesta = respuesta,
            fechaCreacion = fechaCreacion,
            leido = leido
        )
    }

    fun MensajeDto.toDomain(): Mensaje {
        return Mensaje(
            mensajeId = mensajeId,
            usuarioId = usuarioId,
            nombreUsuario = nombreUsuario,
            asunto = asunto,
            contenido = contenido,
            respuesta = respuesta,
            fechaCreacion = fechaCreacion ?: "",
            leido = leido
        )
    }

    fun List<MensajeDto>.toEntityList() = map { it.toEntity() }
    fun List<MensajeEntity>.toDomainList() = map { it.toDomain() }
}
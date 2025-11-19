package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioResponse
import edu.ucne.kias_rent_car.domain.model.Usuario
object UsuarioMapper {
    fun UsuarioResponse.toDomain(): Usuario? {
        return try {
            val id = this.usuarioId ?: return null
            val userName = this.userName?.trim() ?: return null

            Usuario(
                id = id,
                userName = userName
            )
        } catch (e: Exception) {
            null
        }
    }

    fun crearUsuarioRequest(
        userName: String,
        password: String,
        usuarioId: Int? = null
    ): UsuarioRequest {
        return UsuarioRequest(
            usuarioId = usuarioId,
            userName = userName.trim(),
            password = password
        )
    }

    fun List<UsuarioResponse>.toDomainList(): List<Usuario> {
        return this.mapNotNull { it.toDomain() }
    }
}
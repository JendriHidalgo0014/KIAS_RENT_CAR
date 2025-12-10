package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.UsuarioDto
import edu.ucne.kias_rent_car.domain.model.Usuario

object UsuarioMapper {
    fun UsuarioDto.toEntity(isLoggedIn: Boolean = false): UsuarioEntity {
        return UsuarioEntity(
            usuarioId = this.usuarioId,
            nombre = this.nombre,
            email = this.email,
            password = this.password ?: "",
            telefono = this.telefono,
            rol = this.rol,
            fechaRegistro = this.fechaRegistro ?: "",
            isLoggedIn = isLoggedIn
        )
    }
    // Entity -> Domain
    fun UsuarioEntity.toDomain(): Usuario {
        return Usuario(
            id = this.usuarioId,
            nombre = this.nombre,
            email = this.email,
            telefono = this.telefono,
            rol = this.rol
        )
    }
    // DTO -> Domain
    fun UsuarioDto.toDomain(): Usuario {
        return Usuario(
            id = this.usuarioId,
            nombre = this.nombre,
            email = this.email,
            telefono = this.telefono,
            rol = this.rol
        )
    }
}
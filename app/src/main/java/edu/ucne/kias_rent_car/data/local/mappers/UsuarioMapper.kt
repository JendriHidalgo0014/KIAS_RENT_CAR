package edu.ucne.kias_rent_car.data.mappers

import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.remote.dto.UsuarioDto
import edu.ucne.kias_rent_car.domain.model.Usuario
import java.util.UUID

object UsuarioMapper {

    fun UsuarioEntity.toDomain(): Usuario = Usuario(
        id = id,
        remoteId = remoteId,
        nombre = nombre,
        email = email,
        telefono = telefono,
        rol = rol
    )

    fun Usuario.toEntity(password: String = "", isLoggedIn: Boolean = false): UsuarioEntity = UsuarioEntity(
        id = id,
        remoteId = remoteId,
        nombre = nombre,
        email = email,
        password = password,
        telefono = telefono,
        rol = rol,
        isLoggedIn = isLoggedIn
    )

    fun UsuarioDto.toDomain(): Usuario = Usuario(
        id = UUID.randomUUID().toString(),
        remoteId = usuarioId,
        nombre = nombre,
        email = email,
        telefono = telefono,
        rol = rol ?: "Cliente"
    )

    fun UsuarioDto.toEntity(isLoggedIn: Boolean = false): UsuarioEntity = UsuarioEntity(
        id = UUID.randomUUID().toString(),
        remoteId = usuarioId,
        nombre = nombre,
        email = email,
        password = "",
        telefono = telefono,
        rol = rol ?: "Cliente",
        isLoggedIn = isLoggedIn
    )

    fun List<UsuarioEntity>.toDomainList(): List<Usuario> = map { it.toDomain() }
    fun List<UsuarioDto>.toDomainList(): List<Usuario> = map { it.toDomain() }
}
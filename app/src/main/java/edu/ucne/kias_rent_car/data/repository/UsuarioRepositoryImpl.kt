package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.toEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.UsuarioRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: UsuarioRemoteDataSource,
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {
    override suspend fun login(email: String, password: String): Resource<Usuario> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return Resource.Error("Email y contraseña son requeridos")
            }
            val usuarioDto = remoteDataSource.login(email.trim(), password)

            if (usuarioDto != null) {
                usuarioDao.logoutAll()
                usuarioDao.insertUsuario(usuarioDto.toEntity(isLoggedIn = true))
                Resource.Success(usuarioDto.toDomain())
            } else {
                val usuarioLocal = usuarioDao.getUsuarioByEmail(email.trim())
                if (usuarioLocal != null && usuarioLocal.password == password) {
                    usuarioDao.logoutAll()
                    usuarioDao.setLoggedIn(usuarioLocal.usuarioId)
                    Resource.Success(usuarioLocal.toDomain())
                } else {
                    Resource.Error("Email o contraseña incorrectos")
                }
            }
        } catch (e: Exception) {
            try {
                val usuarioLocal = usuarioDao.getUsuarioByEmail(email.trim())
                if (usuarioLocal != null && usuarioLocal.password == password) {
                    usuarioDao.logoutAll()
                    usuarioDao.setLoggedIn(usuarioLocal.usuarioId)
                    Resource.Success(usuarioLocal.toDomain())
                } else {
                    Resource.Error("Sin conexión. Verifica tu internet")
                }
            } catch (e: Exception) {
                Resource.Error("Error de conexión")
            }
        }
    }

    override suspend fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        telefono: String?
    ): Resource<Usuario> {
        return try {
            if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                return Resource.Error("Todos los campos son requeridos")
            }

            if (password.length < 4) {
                return Resource.Error("La contraseña debe tener al menos 4 caracteres")
            }

            val usuarioDto = remoteDataSource.registro(
                nombre = nombre.trim(),
                email = email.trim(),
                password = password,
                telefono = telefono?.trim()
            )

            if (usuarioDto != null) {
                usuarioDao.logoutAll()
                usuarioDao.insertUsuario(usuarioDto.toEntity(isLoggedIn = true))

                Resource.Success(usuarioDto.toDomain())
            } else {
                Resource.Error("No se pudo crear el usuario. El email puede estar en uso.")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión. Verifica tu internet")
        }
    }

    override suspend fun logout() {
        usuarioDao.logoutAll()
    }

    override suspend fun getUsuarioLogueado(): Usuario? {
        return usuarioDao.getLoggedInUsuario()?.toDomain()
    }

    override fun observeUsuarioLogueado(): Flow<Usuario?> {
        return usuarioDao.observeLoggedInUsuario().map { it?.toDomain() }
    }

    override suspend fun getUsuarioById(id: Int): Resource<Usuario> {
        return try {
            val local = usuarioDao.getUsuarioById(id)
            if (local != null) {
                return Resource.Success(local.toDomain())
            }
            val remoto = remoteDataSource.getUsuarioById(id)
            if (remoto != null) {
                usuarioDao.insertUsuario(remoto.toEntity())
                Resource.Success(remoto.toDomain())
            } else {
                Resource.Error("Usuario no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error("Error al obtener usuario")
        }
    }

    override suspend fun getAllUsuarios(): List<Usuario> {
        return try {
            val remotos = remoteDataSource.getUsuarios()
            if (remotos != null) {
                remotos.forEach { dto ->
                    usuarioDao.insertUsuario(dto.toEntity())
                }
                remotos.map { it.toDomain() }
            } else {
                usuarioDao.getAllUsuarios().map { it.toDomain() }
            }
        } catch (e: Exception) {
            try {
                usuarioDao.getAllUsuarios().map { it.toDomain() }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun deleteUsuario(id: Int): Boolean {
        return try {
            remoteDataSource.deleteUsuario(id)
        } catch (e: Exception) {
            false
        }
    }
}
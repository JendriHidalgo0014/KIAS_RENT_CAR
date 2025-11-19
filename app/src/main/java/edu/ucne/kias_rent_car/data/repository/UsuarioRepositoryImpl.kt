package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.crearUsuarioRequest
import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.toDomain
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.UsuarioRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: UsuarioRemoteDataSource
) : UsuarioRepository {

    override suspend fun login(userName: String, password: String): Resource<Usuario> {
        return try {
            if (userName.isBlank() || password.isBlank()) {
                return Resource.Error("Usuario y contraseña son requeridos")
            }

            val usuarioDto = remoteDataSource.login(userName, password)

            if (usuarioDto != null) {
                val usuario = usuarioDto.toDomain()
                if (usuario != null) {
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Error al procesar datos del usuario")
                }
            } else {
                Resource.Error("Usuario o contraseña incorrectos")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión. Verifica tu internet")
        }
    }

    override suspend fun registrarUsuario(
        userName: String,
        password: String
    ): Resource<Usuario> {
        return try {
            if (userName.isBlank() || password.isBlank()) {
                return Resource.Error("Todos los campos son requeridos")
            }

            if (password.length < 4) {
                return Resource.Error("La contraseña debe tener al menos 4 caracteres")
            }

            val existe = remoteDataSource.verificarUserNameExistente(userName)
            if (existe) {
                return Resource.Error("El nombre de usuario ya está registrado")
            }

            val request = crearUsuarioRequest(userName, password)
            val usuarioRegistrado = remoteDataSource.registrarUsuario(request)

            if (usuarioRegistrado != null) {
                val usuario = usuarioRegistrado.toDomain()
                if (usuario != null) {
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Error al crear usuario")
                }
            } else {
                Resource.Error("No se pudo crear el usuario")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión. Verifica tu internet")
        }
    }

    override suspend fun verificarUserNameExistente(userName: String): Boolean {
        return try {
            remoteDataSource.verificarUserNameExistente(userName)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun obtenerUsuarioPorId(id: Int): Resource<Usuario> {
        return try {
            val usuarioDto = remoteDataSource.obtenerUsuarioPorId(id)
            if (usuarioDto != null) {
                val usuario = usuarioDto.toDomain()
                if (usuario != null) {
                    Resource.Success(usuario)
                } else {
                    Resource.Error("Usuario no encontrado")
                }
            } else {
                Resource.Error("Usuario no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error("Error de conexión")
        }
    }
}
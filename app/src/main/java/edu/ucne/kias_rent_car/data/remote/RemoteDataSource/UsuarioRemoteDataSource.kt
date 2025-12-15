package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.dto.LoginRequest
import edu.ucne.kias_rent_car.data.remote.dto.RegistroRequest
import edu.ucne.kias_rent_car.data.remote.dto.UsuarioDto
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val api: ApiService
) {
    suspend fun login(email: String, password: String): Resource<UsuarioDto> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Credenciales inválidas")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun registro(
        nombre: String,
        email: String,
        password: String,
        telefono: String?
    ): Resource<UsuarioDto> {
        return try {
            val request = RegistroRequest(
                nombre = nombre,
                email = email,
                password = password,
                telefono = telefono,
                rol = "Cliente"
            )
            val response = api.registro(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Error al registrar usuario")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getUsuarioById(id: Int): Resource<UsuarioDto> {
        return try {
            val response = api.getUsuarioById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Usuario no encontrado")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getUsuarios(): Resource<List<UsuarioDto>> {
        return try {
            val response = api.getUsuarios()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteUsuario(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}
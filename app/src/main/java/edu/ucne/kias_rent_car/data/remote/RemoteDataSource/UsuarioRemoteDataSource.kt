package edu.ucne.kias_rent_car.data.remote

import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.LoginRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.RegistroRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.UsuarioDto
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(email: String, password: String): UsuarioDto? {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    suspend fun registro(
        nombre: String,
        email: String,
        password: String,
        telefono: String?
    ): UsuarioDto? {
        return try {
            val request = RegistroRequest(
                nombre = nombre,
                email = email,
                password = password,
                telefono = telefono,
                rol = "Cliente"
            )
            val response = apiService.registro(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getUsuarioById(id: Int): UsuarioDto? {
        return try {
            val response = apiService.getUsuarioById(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUsuarios(): List<UsuarioDto>? {
        return try {
            val response = apiService.getUsuarios()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteUsuario(id: Int): Boolean {
        return try {
            val response = apiService.deleteUsuario(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
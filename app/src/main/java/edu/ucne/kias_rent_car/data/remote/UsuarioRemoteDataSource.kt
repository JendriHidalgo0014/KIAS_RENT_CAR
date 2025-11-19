package edu.ucne.kias_rent_car.data.remote

import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioResponse
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(userName: String, password: String): UsuarioResponse? {
        return try {
            val response = apiService.obtenerUsuarios()
            if (response.isSuccessful) {
                val usuarios = response.body() ?: emptyList()
                usuarios.find {
                    it.userName?.trim() == userName.trim() &&
                            it.password?.trim() == password.trim()
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Registrar nuevo usuario
     */
    suspend fun registrarUsuario(request: UsuarioRequest): UsuarioResponse? {
        return try {
            val response = apiService.crearUsuario(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun obtenerUsuarioPorId(id: Int): UsuarioResponse? {
        return try {
            val response = apiService.obtenerUsuarioPorId(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun verificarUserNameExistente(userName: String): Boolean {
        return try {
            val response = apiService.obtenerUsuarios()
            if (response.isSuccessful) {
                val usuarios = response.body() ?: emptyList()
                usuarios.any { it.userName?.trim() == userName.trim() }
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
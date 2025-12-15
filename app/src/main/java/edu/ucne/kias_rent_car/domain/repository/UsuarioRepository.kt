package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun login(email: String, password: String): Resource<Usuario>
    suspend fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        telefono: String?
    ): Resource<Usuario>
    suspend fun logout(): Resource<Unit>
    suspend fun getUsuarioLogueado(): Resource<Usuario?>
    fun observeUsuarioLogueado(): Flow<Usuario?>
    suspend fun getUsuarioById(id: String): Resource<Usuario>
    suspend fun getAllUsuarios(): Resource<List<Usuario>>
    suspend fun deleteUsuario(id: String): Resource<Unit>
}
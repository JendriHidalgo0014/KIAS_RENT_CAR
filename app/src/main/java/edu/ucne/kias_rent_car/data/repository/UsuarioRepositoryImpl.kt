package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.UsuarioMapper.toEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.UsuarioRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val localDataSource: UsuarioDao,
    private val remoteDataSource: UsuarioRemoteDataSource
) : UsuarioRepository {

    override suspend fun login(email: String, password: String): Resource<Usuario> {
        if (email.isBlank() || password.isBlank()) {
            return Resource.Error("Email y contraseña son requeridos")
        }

        return when (val result = remoteDataSource.login(email.trim(), password)) {
            is Resource.Success -> {
                localDataSource.logoutAll()
                val entity = result.data.toEntity(isLoggedIn = true)
                localDataSource.insertUsuario(entity)
                Resource.Success(result.data.toDomain())
            }
            is Resource.Error -> loginLocal(email, password)
            is Resource.Loading -> Resource.Loading()
        }
    }

    private suspend fun loginLocal(email: String, password: String): Resource<Usuario> {
        val usuarioLocal = localDataSource.getByEmail(email.trim())
        return if (usuarioLocal != null && usuarioLocal.password == password) {
            localDataSource.logoutAll()
            localDataSource.setLoggedIn(usuarioLocal.id)
            Resource.Success(usuarioLocal.toDomain())
        } else {
            Resource.Error("Email o contraseña incorrectos")
        }
    }

    override suspend fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        telefono: String?
    ): Resource<Usuario> {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            return Resource.Error("Todos los campos son requeridos")
        }
        if (password.length < 4) {
            return Resource.Error("La contraseña debe tener al menos 4 caracteres")
        }

        return when (val result = remoteDataSource.registro(
            nombre = nombre.trim(),
            email = email.trim(),
            password = password,
            telefono = telefono?.trim()
        )) {
            is Resource.Success -> {
                localDataSource.logoutAll()
                val entity = result.data.toEntity(isLoggedIn = true)
                localDataSource.insertUsuario(entity)
                Resource.Success(result.data.toDomain())
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun logout(): Resource<Unit> {
        localDataSource.logoutAll()
        return Resource.Success(Unit)
    }

    override suspend fun getUsuarioLogueado(): Resource<Usuario?> {
        val usuario = localDataSource.getLoggedInUsuario()?.toDomain()
        return Resource.Success(usuario)
    }

    override fun observeUsuarioLogueado(): Flow<Usuario?> {
        return localDataSource.observeLoggedInUsuario().map { it?.toDomain() }
    }

    override suspend fun getUsuarioById(id: String): Resource<Usuario> {
        val local = localDataSource.getById(id)
        if (local != null) {
            return Resource.Success(local.toDomain())
        }

        val remoteId = id.toIntOrNull()
        if (remoteId != null) {
            val byRemote = localDataSource.getByRemoteId(remoteId)
            if (byRemote != null) {
                return Resource.Success(byRemote.toDomain())
            }

            return when (val result = remoteDataSource.getUsuarioById(remoteId)) {
                is Resource.Success -> {
                    localDataSource.insertUsuario(result.data.toEntity())
                    Resource.Success(result.data.toDomain())
                }
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading()
            }
        }
        return Resource.Error("ID inválido")
    }

    override suspend fun getAllUsuarios(): Resource<List<Usuario>> {
        return when (val result = remoteDataSource.getUsuarios()) {
            is Resource.Success -> {
                result.data.forEach { dto -> localDataSource.insertUsuario(dto.toEntity()) }
                Resource.Success(result.data.toDomainList())
            }
            is Resource.Error -> {
                val locales = localDataSource.getAllUsuarios()
                if (locales.isNotEmpty()) {
                    Resource.Success(locales.toDomainList())
                } else {
                    Resource.Error(result.message)
                }
            }
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun deleteUsuario(id: String): Resource<Unit> {
        val usuario = localDataSource.getById(id)
        val remoteId = usuario?.remoteId ?: id.toIntOrNull()

        return if (remoteId != null) {
            when (val result = remoteDataSource.deleteUsuario(remoteId)) {
                is Resource.Success -> {
                    localDataSource.deleteById(id)
                    Resource.Success(Unit)
                }
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading()
            }
        } else {
            localDataSource.deleteById(id)
            Resource.Success(Unit)
        }
    }
}
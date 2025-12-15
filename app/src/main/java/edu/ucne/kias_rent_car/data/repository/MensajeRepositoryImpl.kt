package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.entities.MensajeEntity
import edu.ucne.kias_rent_car.data.mappers.MensajeMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.MensajeMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.MensajeMapper.toEntityList
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.MensajeRemoteDataSource
import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class MensajeRepositoryImpl @Inject constructor(
    private val localDataSource: MensajeDao,
    private val remoteDataSource: MensajeRemoteDataSource,
    private val usuarioDao: UsuarioDao
) : MensajeRepository {

    private val errorDesconocido = "Error desconocido"
    private val errorObtenerMensajes = "Error al obtener mensajes"
    private val errorMensajeNoEncontrado = "Mensaje no encontrado"
    private val nombreUsuarioDefecto = "Usuario"
    private val usuarioIdDefecto = 1

    override suspend fun getMensajes(): Resource<List<Mensaje>> {
        return try {
            when (val result = remoteDataSource.getMensajes()) {
                is Resource.Success -> {
                    val pendingIds = (localDataSource.getPendingCreate() + localDataSource.getPendingUpdate())
                        .mapNotNull { it.remoteId }.toSet()
                    val toInsert = result.data?.toEntityList()?.filter { it.remoteId !in pendingIds } ?: emptyList()
                    localDataSource.insertMensajes(toInsert)
                    Resource.Success(localDataSource.getMensajes().toDomainList())
                }
                is Resource.Error -> {
                    val locales = localDataSource.getMensajes()
                    if (locales.isNotEmpty()) {
                        Resource.Success(locales.toDomainList())
                    } else {
                        Resource.Error(result.message ?: errorDesconocido)
                    }
                }
                is Resource.Loading -> Resource.Loading()
            }
        } catch (e: Exception) {
            val locales = localDataSource.getMensajes()
            if (locales.isNotEmpty()) {
                Resource.Success(locales.toDomainList())
            } else {
                Resource.Error(e.localizedMessage ?: errorObtenerMensajes)
            }
        }
    }

    override suspend fun getMensajeById(id: String): Resource<Mensaje> {
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

            return when (val result = remoteDataSource.getMensajeById(remoteId)) {
                is Resource.Success -> Resource.Success(result.data!!.toDomain())
                is Resource.Error -> Resource.Error(result.message ?: errorDesconocido)
                is Resource.Loading -> Resource.Loading()
            }
        }

        return Resource.Error(errorMensajeNoEncontrado)
    }

    override suspend fun getMensajesByUsuario(usuarioId: Int): Resource<List<Mensaje>> {
        return try {
            when (val result = remoteDataSource.getMensajesByUsuario(usuarioId)) {
                is Resource.Success -> {
                    result.data?.toEntityList()?.let { localDataSource.insertMensajes(it) }
                    Resource.Success(localDataSource.getMensajesByUsuario(usuarioId).toDomainList())
                }
                is Resource.Error -> Resource.Success(localDataSource.getMensajesByUsuario(usuarioId).toDomainList())
                is Resource.Loading -> Resource.Loading()
            }
        } catch (e: Exception) {
            Resource.Success(localDataSource.getMensajesByUsuario(usuarioId).toDomainList())
        }
    }

    override suspend fun createMensajeLocal(asunto: String, contenido: String): Resource<Mensaje> {
        val usuarioLogueado = usuarioDao.getLoggedInUsuario()
        val usuarioId = usuarioLogueado?.remoteId ?: usuarioIdDefecto
        val nombreUsuario = usuarioLogueado?.nombre ?: nombreUsuarioDefecto

        val entity = MensajeEntity(
            id = UUID.randomUUID().toString(),
            remoteId = null,
            usuarioId = usuarioId,
            nombreUsuario = nombreUsuario,
            asunto = asunto,
            contenido = contenido,
            respuesta = null,
            fechaCreacion = LocalDateTime.now().toString(),
            leido = false,
            isPendingCreate = true,
            isPendingUpdate = false
        )
        localDataSource.insertMensaje(entity)
        return Resource.Success(entity.toDomain())
    }

    override suspend fun sendMensaje(asunto: String, contenido: String): Resource<Mensaje> {
        val usuarioLogueado = usuarioDao.getLoggedInUsuario()
        val usuarioId = usuarioLogueado?.remoteId ?: usuarioIdDefecto

        return when (val result = remoteDataSource.sendMensaje(usuarioId, asunto, contenido)) {
            is Resource.Success -> {
                val entity = MensajeEntity(
                    id = UUID.randomUUID().toString(),
                    remoteId = result.data?.mensajeId,
                    usuarioId = usuarioId,
                    nombreUsuario = usuarioLogueado?.nombre ?: nombreUsuarioDefecto,
                    asunto = asunto,
                    contenido = contenido,
                    respuesta = null,
                    fechaCreacion = result.data?.fechaCreacion ?: LocalDateTime.now().toString(),
                    leido = false,
                    isPendingCreate = false,
                    isPendingUpdate = false
                )
                localDataSource.insertMensaje(entity)
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> createMensajeLocal(asunto, contenido)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun responderMensaje(mensajeId: String, respuesta: String): Resource<Unit> {
        localDataSource.updateRespuestaLocal(mensajeId, respuesta)

        val mensaje = localDataSource.getById(mensajeId)
        val remoteId = mensaje?.remoteId

        return if (remoteId != null) {
            when (val result = remoteDataSource.responderMensaje(remoteId, respuesta)) {
                is Resource.Success -> {
                    localDataSource.markAsUpdated(mensajeId)
                    Resource.Success(Unit)
                }
                else -> Resource.Success(Unit)
            }
        } else {
            Resource.Success(Unit)
        }
    }

    override suspend fun postPendingMensajes(): Resource<Unit> {
        val pendingCreate = localDataSource.getPendingCreate()
        for (mensaje in pendingCreate) {
            when (val result = remoteDataSource.sendMensaje(
                usuarioId = mensaje.usuarioId,
                asunto = mensaje.asunto,
                contenido = mensaje.contenido
            )) {
                is Resource.Success -> result.data?.mensajeId?.let {
                    localDataSource.markAsCreated(mensaje.id, it)
                }
                else -> Unit
            }
        }

        val pendingUpdate = localDataSource.getPendingUpdate()
        for (mensaje in pendingUpdate) {
            val remoteId = mensaje.remoteId ?: continue
            when (remoteDataSource.responderMensaje(remoteId, mensaje.respuesta ?: "")) {
                is Resource.Success -> localDataSource.markAsUpdated(mensaje.id)
                else -> Unit
            }
        }

        return Resource.Success(Unit)
    }
}
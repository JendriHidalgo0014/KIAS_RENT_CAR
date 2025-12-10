package edu.ucne.kias_rent_car.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.entity.MensajeEntity
import edu.ucne.kias_rent_car.data.mappers.MensajeMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.MensajeMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.MensajeMapper.toEntityList
import edu.ucne.kias_rent_car.data.remote.datasource.MensajeRemoteDataSource
import edu.ucne.kias_rent_car.data.sync.SyncTrigger
import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import javax.inject.Inject
//Se le agregó Logs al codigo porque se me presentaban errores que no me aparecian ni
//en el build ni en el Logcat, de no ser por eso no los pongo.
class MensajeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: MensajeRemoteDataSource,
    private val mensajeDao: MensajeDao,
    private val usuarioDao: UsuarioDao
) : MensajeRepository {
    companion object {
        private const val TAG = "MensajeRepo"
    }

    override suspend fun getMensajes(): List<Mensaje> {
        try {
            val remotos = remoteDataSource.getMensajes()
            if (remotos != null && remotos.isNotEmpty()) {
                val existingPending = mensajeDao.getPendingCreate() + mensajeDao.getPendingRespuesta()
                val pendingIds = existingPending.map { it.mensajeId }.toSet()

                val toInsert = remotos.toEntityList().filter { it.mensajeId !in pendingIds }
                mensajeDao.insertMensajes(toInsert)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo remotos: ${e.message}")
        }
        return mensajeDao.getMensajes().toDomainList()
    }

    override suspend fun getMensajeById(id: Int): Mensaje? {
        try {
            val remoto = remoteDataSource.getMensajeById(id)
            if (remoto != null) {
                return remoto.toDomain()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
        return mensajeDao.getMensajeById(id)?.toDomain()
    }

    override suspend fun sendMensaje(asunto: String, contenido: String): Mensaje? {
        val usuarioLogueado = usuarioDao.getLoggedInUsuario()
        val usuarioId = usuarioLogueado?.usuarioId ?: 1
        val nombreUsuario = usuarioLogueado?.nombre ?: "Usuario"
        val entityLocal = MensajeEntity(
            mensajeId = 0,
            remoteId = null,
            usuarioId = usuarioId,
            nombreUsuario = nombreUsuario,
            asunto = asunto,
            contenido = contenido,
            respuesta = null,
            fechaCreacion = java.time.LocalDateTime.now().toString(),
            leido = false,
            isPendingCreate = true
        )
        mensajeDao.insertMensaje(entityLocal)
        Log.d(TAG, "Mensaje guardado localmente")

        return try {
            val response = remoteDataSource.sendMensaje(
                usuarioId = usuarioId,
                asunto = asunto,
                contenido = contenido
            )

            if (response != null) {
                val updatedEntity = entityLocal.copy(
                    remoteId = response.mensajeId,
                    isPendingCreate = false
                )
                mensajeDao.insertMensaje(updatedEntity)
                Log.d(TAG, "Mensaje sincronizado con ID: ${response.mensajeId}")
                response.toDomain()
            } else {
                SyncTrigger.triggerImmediateSync(context)
                entityLocal.toDomain()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error de conexión: ${e.message}")
            SyncTrigger.triggerImmediateSync(context)
            entityLocal.toDomain()
        }
    }

    override suspend fun responderMensaje(mensajeId: Int, respuesta: String) {
        mensajeDao.updateRespuestaLocal(mensajeId, respuesta)
        Log.d(TAG, "Respuesta guardada localmente para mensaje $mensajeId")

        try {
            val success = remoteDataSource.responderMensaje(mensajeId, respuesta)
            if (success) {
                mensajeDao.markRespuestaAsSynced(mensajeId)
                Log.d(TAG, "Respuesta sincronizada")
            } else {
                SyncTrigger.triggerImmediateSync(context)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error de conexión: ${e.message}")
            SyncTrigger.triggerImmediateSync(context)
        }
    }

    override suspend fun getMensajesByUsuario(usuarioId: Int): List<Mensaje> {
        try {
            val remotos = remoteDataSource.getMensajesByUsuario(usuarioId)
            if (remotos != null && remotos.isNotEmpty()) {
                mensajeDao.insertMensajes(remotos.toEntityList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
        return mensajeDao.getMensajesByUsuario(usuarioId).toDomainList()
    }
}
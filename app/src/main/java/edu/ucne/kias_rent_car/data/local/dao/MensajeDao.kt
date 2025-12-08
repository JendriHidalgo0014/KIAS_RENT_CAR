package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.*
import edu.ucne.kias_rent_car.data.local.entity.MensajeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMensaje(mensaje: MensajeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMensajes(mensajes: List<MensajeEntity>)

    @Query("SELECT * FROM mensajes ORDER BY fechaCreacion DESC")
    suspend fun getMensajes(): List<MensajeEntity>

    @Query("SELECT * FROM mensajes ORDER BY fechaCreacion DESC")
    fun observeMensajes(): Flow<List<MensajeEntity>>

    @Query("SELECT * FROM mensajes WHERE mensajeId = :id")
    suspend fun getMensajeById(id: Int): MensajeEntity?

    @Query("SELECT * FROM mensajes WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    suspend fun getMensajesByUsuario(usuarioId: Int): List<MensajeEntity>

    @Query("UPDATE mensajes SET respuesta = :respuesta, isPendingRespuesta = 1 WHERE mensajeId = :id")
    suspend fun updateRespuestaLocal(id: Int, respuesta: String)

    @Query("DELETE FROM mensajes")
    suspend fun deleteAll()

    @Query("SELECT * FROM mensajes WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<MensajeEntity>

    @Query("SELECT * FROM mensajes WHERE isPendingRespuesta = 1")
    suspend fun getPendingRespuesta(): List<MensajeEntity>

    @Query("UPDATE mensajes SET isPendingCreate = 0, remoteId = :remoteId WHERE mensajeId = :localId")
    suspend fun markAsCreated(localId: Int, remoteId: Int)

    @Query("UPDATE mensajes SET isPendingRespuesta = 0 WHERE mensajeId = :id")
    suspend fun markRespuestaAsSynced(id: Int)
}
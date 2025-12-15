package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.*
import edu.ucne.kias_rent_car.data.local.entities.MensajeEntity
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

    @Query("SELECT * FROM mensajes WHERE id = :id")
    suspend fun getById(id: String): MensajeEntity?

    @Query("SELECT * FROM mensajes WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): MensajeEntity?

    @Query("SELECT * FROM mensajes WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    suspend fun getMensajesByUsuario(usuarioId: Int): List<MensajeEntity>

    @Query("UPDATE mensajes SET respuesta = :respuesta, isPendingUpdate = 1 WHERE id = :id")
    suspend fun updateRespuestaLocal(id: String, respuesta: String)

    @Query("DELETE FROM mensajes")
    suspend fun deleteAll()

    @Query("DELETE FROM mensajes WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM mensajes WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<MensajeEntity>

    @Query("SELECT * FROM mensajes WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<MensajeEntity>

    @Query("UPDATE mensajes SET isPendingCreate = 0, remoteId = :remoteId WHERE id = :localId")
    suspend fun markAsCreated(localId: String, remoteId: Int)

    @Query("UPDATE mensajes SET isPendingUpdate = 0 WHERE id = :id")
    suspend fun markAsUpdated(id: String)
}
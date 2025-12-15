package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.*
import edu.ucne.kias_rent_car.data.local.entities.ReservacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservacion(reservacion: ReservacionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservaciones(reservaciones: List<ReservacionEntity>)

    @Update
    suspend fun updateReservacion(reservacion: ReservacionEntity)

    @Query("SELECT * FROM reservaciones ORDER BY fechaCreacion DESC")
    suspend fun getReservaciones(): List<ReservacionEntity>

    @Query("SELECT * FROM reservaciones ORDER BY fechaCreacion DESC")
    fun observeAll(): Flow<List<ReservacionEntity>>

    @Query("SELECT * FROM reservaciones WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    suspend fun getReservacionesByUsuario(usuarioId: Int): List<ReservacionEntity>

    @Query("SELECT * FROM reservaciones WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun observeByUsuario(usuarioId: Int): Flow<List<ReservacionEntity>>

    @Query("SELECT * FROM reservaciones WHERE id = :id")
    suspend fun getById(id: String): ReservacionEntity?

    @Query("SELECT * FROM reservaciones WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): ReservacionEntity?

    @Query("DELETE FROM reservaciones")
    suspend fun deleteAll()

    @Query("DELETE FROM reservaciones WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE reservaciones SET estado = :estado, isPendingUpdate = 1 WHERE id = :id")
    suspend fun updateEstadoLocal(id: String, estado: String)

    @Query("""
        UPDATE reservaciones SET 
            ubicacionRecogidaId = :ubicacionRecogidaId,
            ubicacionDevolucionId = :ubicacionDevolucionId,
            fechaRecogida = :fechaRecogida,
            horaRecogida = :horaRecogida,
            fechaDevolucion = :fechaDevolucion,
            horaDevolucion = :horaDevolucion,
            isPendingUpdate = 1
        WHERE id = :id
    """)
    suspend fun updateDatosLocal(
        id: String,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    )

    @Query("SELECT * FROM reservaciones WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<ReservacionEntity>

    @Query("SELECT * FROM reservaciones WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<ReservacionEntity>

    @Query("SELECT * FROM reservaciones WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<ReservacionEntity>

    @Query("UPDATE reservaciones SET isPendingCreate = 0, remoteId = :remoteId WHERE id = :localId")
    suspend fun markAsCreated(localId: String, remoteId: Int)

    @Query("UPDATE reservaciones SET isPendingUpdate = 0 WHERE id = :id")
    suspend fun markAsUpdated(id: String)

    @Query("UPDATE reservaciones SET isPendingDelete = 0 WHERE id = :id")
    suspend fun markAsDeleted(id: String)
}
package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.*
import edu.ucne.kias_rent_car.data.local.entity.ReservacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservacion(reservacion: ReservacionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservaciones(reservaciones: List<ReservacionEntity>)

    @Update
    suspend fun updateReservacion(reservacion: ReservacionEntity)

    @Query("SELECT * FROM reservaciones WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun observeByUsuario(usuarioId: Int): Flow<List<ReservacionEntity>>

    @Query("SELECT * FROM reservaciones ORDER BY fechaCreacion DESC")
    fun observeAll(): Flow<List<ReservacionEntity>>

    @Query("SELECT * FROM reservaciones WHERE reservacionId = :id")
    suspend fun getById(id: Int): ReservacionEntity?

    @Query("DELETE FROM reservaciones")
    suspend fun deleteAll()

    @Query("UPDATE reservaciones SET estado = :estado, isPendingEstadoUpdate = 1 WHERE reservacionId = :id")
    suspend fun updateEstadoLocal(id: Int, estado: String)

    @Query("""
        UPDATE reservaciones SET 
            ubicacionRecogidaId = :ubicacionRecogidaId,
            ubicacionDevolucionId = :ubicacionDevolucionId,
            fechaRecogida = :fechaRecogida,
            horaRecogida = :horaRecogida,
            fechaDevolucion = :fechaDevolucion,
            horaDevolucion = :horaDevolucion,
            isPendingUpdate = 1
        WHERE reservacionId = :id
    """)
    suspend fun updateDatosLocal(
        id: Int,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    )

    @Query("SELECT * FROM reservaciones ORDER BY fechaCreacion DESC")
    suspend fun getReservaciones(): List<ReservacionEntity>

    @Query("SELECT * FROM reservaciones WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    suspend fun getReservacionesByUsuario(usuarioId: Int): List<ReservacionEntity>

    @Query("DELETE FROM reservaciones WHERE reservacionId = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE reservaciones SET estado = :estado WHERE reservacionId = :id")
    suspend fun updateEstado(id: Int, estado: String)

    @Query("""
    UPDATE reservaciones SET 
        ubicacionRecogidaId = :ubicacionRecogidaId,
        ubicacionDevolucionId = :ubicacionDevolucionId,
        fechaRecogida = :fechaRecogida,
        horaRecogida = :horaRecogida,
        fechaDevolucion = :fechaDevolucion,
        horaDevolucion = :horaDevolucion
    WHERE reservacionId = :id
""")
    suspend fun updateReservacionData(
        id: Int,
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

    @Query("SELECT * FROM reservaciones WHERE isPendingEstadoUpdate = 1")
    suspend fun getPendingEstadoUpdate(): List<ReservacionEntity>

    @Query("UPDATE reservaciones SET isPendingCreate = 0, reservacionId = :remoteId WHERE reservacionId = :localId")
    suspend fun markAsCreated(localId: Int, remoteId: Int)

    @Query("UPDATE reservaciones SET isPendingUpdate = 0 WHERE reservacionId = :id")
    suspend fun markAsUpdated(id: Int)

    @Query("UPDATE reservaciones SET isPendingEstadoUpdate = 0 WHERE reservacionId = :id")
    suspend fun markEstadoAsUpdated(id: Int)
}
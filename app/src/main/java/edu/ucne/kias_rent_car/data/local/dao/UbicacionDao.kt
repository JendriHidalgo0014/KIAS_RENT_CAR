package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.ucne.kias_rent_car.data.local.entities.UbicacionEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface UbicacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUbicaciones(ubicaciones: List<UbicacionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUbicacion(ubicacion: UbicacionEntity)

    @Query("SELECT * FROM ubicaciones")
    suspend fun getUbicaciones(): List<UbicacionEntity>

    @Query("SELECT * FROM ubicaciones")
    fun observeUbicaciones(): Flow<List<UbicacionEntity>>

    @Query("SELECT * FROM ubicaciones WHERE id = :id")
    suspend fun getById(id: String): UbicacionEntity?

    @Query("SELECT * FROM ubicaciones WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): UbicacionEntity?

    @Query("SELECT * FROM ubicaciones WHERE nombre = :nombre LIMIT 1")
    suspend fun getByNombre(nombre: String): UbicacionEntity?

    @Query("DELETE FROM ubicaciones")
    suspend fun deleteAll()
}
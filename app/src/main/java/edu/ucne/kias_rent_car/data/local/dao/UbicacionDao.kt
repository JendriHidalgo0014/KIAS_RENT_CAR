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

    @Query("SELECT * FROM ubicaciones WHERE activa = 1")
    suspend fun getUbicaciones(): List<UbicacionEntity>

    @Query("SELECT * FROM ubicaciones WHERE activa = 1")
    fun observeUbicaciones(): Flow<List<UbicacionEntity>>

    @Query("SELECT * FROM ubicaciones WHERE ubicacionId = :id")
    suspend fun getUbicacionById(id: Int): UbicacionEntity?

    @Query("DELETE FROM ubicaciones")
    suspend fun deleteAll()

    @Query("SELECT * FROM ubicaciones WHERE nombre = :nombre LIMIT 1")
    suspend fun getUbicacionByNombre(nombre: String): UbicacionEntity?
}
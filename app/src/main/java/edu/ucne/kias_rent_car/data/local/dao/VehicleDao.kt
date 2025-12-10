package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.*
import edu.ucne.kias_rent_car.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicles")
    fun observeAllVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE categoria = :category")
    fun observeVehiclesByCategory(category: String): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE modelo LIKE :query")
    fun searchVehicles(query: String): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: String): VehicleEntity?

    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAll()

    @Query("UPDATE vehicles SET disponible = :disponible WHERE id = :id")
    suspend fun updateDisponibilidad(id: String, disponible: Boolean)

    @Query("SELECT * FROM vehicles WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<VehicleEntity>

    @Query("SELECT * FROM vehicles WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<VehicleEntity>

    @Query("UPDATE vehicles SET isPendingCreate = 0, remoteId = :remoteId WHERE id = :localId")
    suspend fun markAsCreated(localId: String, remoteId: Int)

    @Query("UPDATE vehicles SET isPendingUpdate = 0 WHERE id = :id")
    suspend fun markAsUpdated(id: String)
}
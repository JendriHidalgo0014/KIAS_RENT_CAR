package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.*
import edu.ucne.kias_rent_car.data.local.entity.ReservationConfigEntity

@Dao
interface ReservationConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: ReservationConfigEntity)

    @Query("SELECT * FROM reservation_config WHERE id = 1")
    suspend fun getConfig(): ReservationConfigEntity?

    @Query("DELETE FROM reservation_config")
    suspend fun clearConfig()
}
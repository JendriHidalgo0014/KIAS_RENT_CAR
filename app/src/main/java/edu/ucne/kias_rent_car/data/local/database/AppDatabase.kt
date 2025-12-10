package edu.ucne.kias_rent_car.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.local.dao.ReservationConfigDao
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.entities.UbicacionEntity
import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.local.entity.MensajeEntity
import edu.ucne.kias_rent_car.data.local.entity.ReservacionEntity
import edu.ucne.kias_rent_car.data.local.entity.ReservationConfigEntity
import edu.ucne.kias_rent_car.data.local.entity.VehicleEntity

@Database(
    entities = [
        UsuarioEntity::class,
        VehicleEntity::class,
        UbicacionEntity::class,
        ReservacionEntity::class,
        MensajeEntity::class,
        ReservationConfigEntity::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun ubicacionDao(): UbicacionDao
    abstract fun reservacionDao(): ReservacionDao
    abstract fun mensajeDao(): MensajeDao
    abstract fun reservationConfigDao(): ReservationConfigDao
}
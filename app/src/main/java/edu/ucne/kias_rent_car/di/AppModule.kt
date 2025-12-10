package edu.ucne.kias_rent_car.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.local.dao.ReservationConfigDao
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "kia_rent_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideUsuarioDao(db: AppDatabase): UsuarioDao = db.usuarioDao()
    @Provides
    fun provideVehicleDao(db: AppDatabase): VehicleDao = db.vehicleDao()
    @Provides
    fun provideUbicacionDao(db: AppDatabase): UbicacionDao = db.ubicacionDao()
    @Provides
    fun provideReservacionDao(db: AppDatabase): ReservacionDao = db.reservacionDao()
    @Provides
    fun provideMensajeDao(db: AppDatabase): MensajeDao = db.mensajeDao()
    @Provides
    fun provideReservationConfigDao(db: AppDatabase): ReservationConfigDao = db.reservationConfigDao()
}
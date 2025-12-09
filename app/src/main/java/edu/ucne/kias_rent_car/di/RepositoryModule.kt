package edu.ucne.kias_rent_car.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.kias_rent_car.data.repository.MensajeRepositoryImpl
import edu.ucne.kias_rent_car.data.repository.ReservacionRepositoryImpl
import edu.ucne.kias_rent_car.data.repository.UbicacionRepositoryImpl
import edu.ucne.kias_rent_car.data.repository.VehicleRepositoryImpl
import edu.ucne.kias_rent_car.data.repository.UsuarioRepositoryImpl
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import edu.ucne.kias_rent_car.domain.repository.UbicacionRepository
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(impl: UsuarioRepositoryImpl): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(impl: VehicleRepositoryImpl): VehicleRepository

    @Binds
    @Singleton
    abstract fun bindUbicacionRepository(impl: UbicacionRepositoryImpl): UbicacionRepository

    @Binds
    @Singleton
    abstract fun bindReservacionRepository(impl: ReservacionRepositoryImpl): ReservacionRepository

    @Binds
    @Singleton
    abstract fun bindMensajeRepository(impl: MensajeRepositoryImpl): MensajeRepository
}
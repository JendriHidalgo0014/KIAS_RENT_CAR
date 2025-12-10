package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.ReservationConfig

interface ReservacionRepository {
    suspend fun getReservaciones(): List<Reservacion>
    suspend fun getReservacionesByUsuario(usuarioId: Int): List<Reservacion>
    suspend fun getReservacionById(id: Int): Reservacion?
    suspend fun createReservacion(config: ReservationConfig): Resource<Reservacion>
    suspend fun updateReservacion(reservacion: Reservacion): Resource<Unit>
    suspend fun updateReservacionData(
        reservacionId: Int,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Resource<Unit>
    suspend fun updateEstado(reservacionId: Int, estado: String): Resource<Unit>
    suspend fun cancelReservacion(reservacionId: Int): Resource<Unit>
    suspend fun saveReservationConfig(config: ReservationConfig)
    suspend fun getReservationConfig(): ReservationConfig?
    suspend fun refreshReservaciones()
}
package edu.ucne.kias_rent_car.domain.repository

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.ReservationConfig

interface ReservacionRepository {
    suspend fun getReservaciones(): Resource<List<Reservacion>>
    suspend fun getReservacionesByUsuario(usuarioId: Int): Resource<List<Reservacion>>
    suspend fun getReservacionById(id: String): Resource<Reservacion>
    suspend fun createReservacionLocal(config: ReservationConfig): Resource<Reservacion>
    suspend fun createReservacion(config: ReservationConfig): Resource<Reservacion>
    suspend fun updateReservacion(
        reservacionId: String,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Resource<Unit>
    suspend fun updateEstado(reservacionId: String, estado: String): Resource<Unit>
    suspend fun cancelReservacion(reservacionId: String): Resource<Unit>
    suspend fun saveReservationConfig(config: ReservationConfig): Resource<Unit>
    suspend fun getReservationConfig(): Resource<ReservationConfig?>
    suspend fun refreshReservaciones(): Resource<Unit>
    suspend fun postPendingReservaciones(): Resource<Unit>
}
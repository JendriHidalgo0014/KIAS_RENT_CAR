package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.EstadoRequest
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.ReservacionDto
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.ReservacionRequest
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.UpdateDatosRequest
import javax.inject.Inject

class ReservacionRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getReservaciones(): List<ReservacionDto>? {
        return try {
            val response = apiService.getReservaciones()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getReservacionesByUsuario(usuarioId: Int): List<ReservacionDto>? {
        return try {
            val response = apiService.getReservacionesByUsuario(usuarioId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getReservacionById(id: Int): ReservacionDto? {
        return try {
            val response = apiService.getReservacionById(id)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun createReservacion(request: ReservacionRequest): ReservacionDto? {
        return try {
            val response = apiService.createReservacion(request)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun updateEstado(reservacionId: Int, estado: String): Boolean {
        return try {
            val response = apiService.updateEstadoReservacion(reservacionId, EstadoRequest(estado))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    suspend fun updateReservacion(
        reservacionId: Int,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Boolean {
        return try {
            val request = UpdateDatosRequest(
                ubicacionRecogidaId = ubicacionRecogidaId,
                ubicacionDevolucionId = ubicacionDevolucionId,
                fechaRecogida = fechaRecogida,
                horaRecogida = horaRecogida,
                fechaDevolucion = fechaDevolucion,
                horaDevolucion = horaDevolucion
            )
            //Puse estos logs para que me mostrara un error en el Logcat que salia de este archivo,
            //esto porque no se queria mostrar aunque me marcaba este archivo. Esto no afecta en mas nada
            //el codigo, solo hace lo que se le pide.
            android.util.Log.d("UPDATE_API", "Enviando: $request")

            val response = apiService.updateReservacionDatos(reservacionId, request)

            android.util.Log.d("UPDATE_API", "Response: ${response.code()}")

            response.isSuccessful
        } catch (e: Exception) {
            android.util.Log.e("UPDATE_API", "Error: ${e.message}", e)
            false
        }
    }
}
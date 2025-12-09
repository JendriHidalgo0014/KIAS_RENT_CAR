package edu.ucne.kias_rent_car.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.EstadoRequest
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.UpdateDatosRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.MensajeRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.RespuestaRequest
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: ApiService,
    private val reservacionDao: ReservacionDao,
    private val mensajeDao: MensajeDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            var hasErrors = false
            hasErrors = syncReservaciones() || hasErrors
            hasErrors = syncMensajes() || hasErrors

            if (hasErrors) Result.retry() else Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
    private suspend fun syncReservaciones(): Boolean {
        var hasErrors = false

        val pendingUpdate = reservacionDao.getPendingUpdate()
        for (reservacion in pendingUpdate) {
            try {
                val request = UpdateDatosRequest(
                    ubicacionRecogidaId = reservacion.ubicacionRecogidaId,
                    ubicacionDevolucionId = reservacion.ubicacionDevolucionId,
                    fechaRecogida = reservacion.fechaRecogida,
                    horaRecogida = reservacion.horaRecogida,
                    fechaDevolucion = reservacion.fechaDevolucion,
                    horaDevolucion = reservacion.horaDevolucion
                )
                val response = apiService.updateReservacionDatos(reservacion.reservacionId, request)
                if (response.isSuccessful) {
                    reservacionDao.markAsUpdated(reservacion.reservacionId)
                } else {
                    hasErrors = true
                }
            } catch (_: Exception) {
                hasErrors = true
            }
        }

        val pendingEstado = reservacionDao.getPendingEstadoUpdate()
        for (reservacion in pendingEstado) {
            try {
                val response = apiService.updateEstadoReservacion(
                    reservacion.reservacionId,
                    EstadoRequest(reservacion.estado)
                )
                if (response.isSuccessful) {
                    reservacionDao.markEstadoAsUpdated(reservacion.reservacionId)
                } else {
                    hasErrors = true
                }
            } catch (_: Exception) {
                hasErrors = true
            }
        }

        return hasErrors
    }
    private suspend fun syncMensajes(): Boolean {
        var hasErrors = false
        val pendingCreate = mensajeDao.getPendingCreate()
        for (mensaje in pendingCreate) {
            try {
                val request = MensajeRequest(
                    usuarioId = mensaje.usuarioId,
                    asunto = mensaje.asunto,
                    contenido = mensaje.contenido
                )
                val response = apiService.createMensaje(request)
                if (response.isSuccessful && response.body() != null) {
                    mensajeDao.markAsCreated(mensaje.mensajeId, response.body()!!.mensajeId)
                } else {
                    hasErrors = true
                }
            } catch (_: Exception) {
                hasErrors = true
            }
        }

        val pendingRespuesta = mensajeDao.getPendingRespuesta()
        for (mensaje in pendingRespuesta) {
            try {
                val remoteId = mensaje.remoteId ?: mensaje.mensajeId
                val response = apiService.responderMensaje(
                    remoteId,
                    RespuestaRequest(mensaje.respuesta ?: "")
                )
                if (response.isSuccessful) {
                    mensajeDao.markRespuestaAsSynced(mensaje.mensajeId)
                } else {
                    hasErrors = true
                }
            } catch (_: Exception) {
                hasErrors = true
            }
        }

        return hasErrors
    }
}
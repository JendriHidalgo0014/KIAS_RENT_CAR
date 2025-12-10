package edu.ucne.kias_rent_car.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.local.dao.ReservationConfigDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.entity.ReservacionEntity
import edu.ucne.kias_rent_car.data.mappers.ReservacionMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.ReservacionMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.ReservacionMapper.toEntityList
import edu.ucne.kias_rent_car.data.mappers.ReservationConfigMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.ReservationConfigMapper.toEntity
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.ReservacionRequest
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.ReservacionRemoteDataSource
import edu.ucne.kias_rent_car.data.sync.SyncTrigger
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class ReservacionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: ReservacionRemoteDataSource,
    private val reservacionDao: ReservacionDao,
    private val reservationConfigDao: ReservationConfigDao,
    private val usuarioDao: UsuarioDao,
    private val vehicleDao: VehicleDao,
    private val ubicacionDao: UbicacionDao
) : ReservacionRepository {
    override suspend fun getReservaciones(): List<Reservacion> {
        try {
            val remotas = remoteDataSource.getReservaciones()
            if (remotas != null && remotas.isNotEmpty()) {
                reservacionDao.insertReservaciones(remotas.toEntityList())
            }
        } catch (_: Exception) { }
        return reservacionDao.getReservaciones().toDomainList()
    }

    override suspend fun getReservacionesByUsuario(usuarioId: Int): List<Reservacion> {
        try {
            val remotas = remoteDataSource.getReservacionesByUsuario(usuarioId)
            if (remotas != null && remotas.isNotEmpty()) {
                reservacionDao.insertReservaciones(remotas.toEntityList())
            }
        } catch (_: Exception) { }
        return reservacionDao.getReservacionesByUsuario(usuarioId).toDomainList()
    }

    override suspend fun getReservacionById(id: Int): Reservacion? {
        try {
            val remota = remoteDataSource.getReservacionById(id)
            if (remota != null) {
                return remota.toDomain()
            }
        } catch (_: Exception) { }
        return reservacionDao.getById(id)?.toDomain()
    }

    override suspend fun createReservacion(config: ReservationConfig): Resource<Reservacion> {
        val usuarioLogueado = usuarioDao.getLoggedInUsuario()
        val usuarioId = usuarioLogueado?.usuarioId ?: 1
        val vehiculo = vehicleDao.getVehicleById(config.vehicleId)
        val ubicacionRecogida = ubicacionDao.getUbicacionByNombre(config.lugarRecogida)
        val ubicacionDevolucion = ubicacionDao.getUbicacionByNombre(config.lugarDevolucion)
        val tempId = -(System.currentTimeMillis() % 100000).toInt()
        val codigoReserva = "KR-${System.currentTimeMillis().toString().takeLast(6)}"

        vehiculo?.let {
            vehicleDao.updateDisponibilidad(config.vehicleId, false)
        }
        val entityLocal = ReservacionEntity(
            reservacionId = tempId,
            usuarioId = usuarioId,
            vehiculoId = config.vehicleId.toIntOrNull() ?: 0,
            fechaRecogida = config.fechaRecogida,
            horaRecogida = config.horaRecogida,
            fechaDevolucion = config.fechaDevolucion,
            horaDevolucion = config.horaDevolucion,
            ubicacionRecogidaId = ubicacionRecogida?.ubicacionId ?: 1,
            ubicacionDevolucionId = ubicacionDevolucion?.ubicacionId ?: 1,
            estado = "Confirmada",
            subtotal = config.subtotal,
            impuestos = config.impuestos,
            total = config.total,
            codigoReserva = codigoReserva,
            fechaCreacion = java.time.LocalDate.now().toString(),
            isPendingCreate = true,
            vehiculoModelo = vehiculo?.modelo ?: "",
            vehiculoImagenUrl = vehiculo?.imagenUrl ?: "",
            vehiculoPrecioPorDia = vehiculo?.precioPorDia ?: 0.0,
            ubicacionRecogidaNombre = config.lugarRecogida,
            ubicacionDevolucionNombre = config.lugarDevolucion
        )
        reservacionDao.insertReservacion(entityLocal)

        return try {
            val request = ReservacionRequest(
                reservaId = 0,
                vehiculoId = config.vehicleId.toIntOrNull() ?: 0,
                usuarioId = usuarioId.toString(),
                fechaRecogida = config.fechaRecogida,
                horaRecogida = config.horaRecogida,
                fechaDevolucion = config.fechaDevolucion,
                horaDevolucion = config.horaDevolucion,
                lugarRecogida = config.lugarRecogida,
                lugarDevolucion = config.lugarDevolucion,
                estado = "Confirmada",
                subtotal = config.subtotal,
                impuestos = config.impuestos,
                total = config.total,
                codigoReserva = codigoReserva
            )
            val response = remoteDataSource.createReservacion(request)
            if (response != null) {
                reservacionDao.deleteById(tempId)
                val entityReal = entityLocal.copy(
                    reservacionId = response.reservacionId,
                    isPendingCreate = false
                )
                reservacionDao.insertReservacion(entityReal)
                Resource.Success(response.toDomain())
            } else {
                SyncTrigger.triggerImmediateSync(context)
                Resource.Success(entityLocal.toDomain())
            }
        } catch (_: Exception) {
            SyncTrigger.triggerImmediateSync(context)
            Resource.Success(entityLocal.toDomain())
        }
    }

    override suspend fun updateReservacionData(
        reservacionId: Int,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Resource<Unit> {
        reservacionDao.updateDatosLocal(
            id = reservacionId,
            ubicacionRecogidaId = ubicacionRecogidaId,
            ubicacionDevolucionId = ubicacionDevolucionId,
            fechaRecogida = fechaRecogida,
            horaRecogida = horaRecogida,
            fechaDevolucion = fechaDevolucion,
            horaDevolucion = horaDevolucion
        )
        return try {
            val success = remoteDataSource.updateReservacion(
                reservacionId = reservacionId,
                ubicacionRecogidaId = ubicacionRecogidaId,
                ubicacionDevolucionId = ubicacionDevolucionId,
                fechaRecogida = fechaRecogida,
                horaRecogida = horaRecogida,
                fechaDevolucion = fechaDevolucion,
                horaDevolucion = horaDevolucion
            )

            if (success) {
                reservacionDao.markAsUpdated(reservacionId)
            } else {
                SyncTrigger.triggerImmediateSync(context)
            }
            Resource.Success(Unit)
        } catch (_: Exception) {
            SyncTrigger.triggerImmediateSync(context)
            Resource.Success(Unit)
        }
    }

    override suspend fun updateReservacion(reservacion: Reservacion): Resource<Unit> {
        return updateReservacionData(
            reservacionId = reservacion.reservacionId,
            ubicacionRecogidaId = reservacion.ubicacionRecogidaId,
            ubicacionDevolucionId = reservacion.ubicacionDevolucionId,
            fechaRecogida = reservacion.fechaRecogida,
            horaRecogida = reservacion.horaRecogida,
            fechaDevolucion = reservacion.fechaDevolucion,
            horaDevolucion = reservacion.horaDevolucion
        )
    }

    override suspend fun updateEstado(reservacionId: Int, estado: String): Resource<Unit> {
        val reservacion = reservacionDao.getById(reservacionId)

        reservacionDao.updateEstadoLocal(reservacionId, estado)
        if (estado == "Cancelada" || estado == "Completada") {
            reservacion?.let {
                vehicleDao.updateDisponibilidad(it.vehiculoId.toString(), true)
            }
        }

        return try {
            val success = remoteDataSource.updateEstado(reservacionId, estado)
            if (success) {
                reservacionDao.markEstadoAsUpdated(reservacionId)
            } else {
                SyncTrigger.triggerImmediateSync(context)
            }
            Resource.Success(Unit)
        } catch (_: Exception) {
            SyncTrigger.triggerImmediateSync(context)
            Resource.Success(Unit)
        }
    }

    override suspend fun cancelReservacion(reservacionId: Int): Resource<Unit> {
        return updateEstado(reservacionId, "Cancelada")
    }

    override suspend fun saveReservationConfig(config: ReservationConfig) {
        reservationConfigDao.saveConfig(config.toEntity())
    }

    override suspend fun getReservationConfig(): ReservationConfig? {
        return reservationConfigDao.getConfig()?.toDomain()
    }

    override suspend fun refreshReservaciones() {
        try {
            val remotas = remoteDataSource.getReservaciones()
            if (remotas != null) {
                val pendingCreate = reservacionDao.getPendingCreate()
                val pendingUpdate = reservacionDao.getPendingUpdate()
                val pendingEstado = reservacionDao.getPendingEstadoUpdate()

                if (pendingCreate.isEmpty() && pendingUpdate.isEmpty() && pendingEstado.isEmpty()) {
                    reservacionDao.deleteAll()
                }
                reservacionDao.insertReservaciones(remotas.toEntityList())
            }
        } catch (_: Exception) { }
    }
}
package edu.ucne.kias_rent_car.data.repository

import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.local.dao.ReservationConfigDao
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.entities.ReservacionEntity
import edu.ucne.kias_rent_car.data.local.entities.UbicacionEntity
import edu.ucne.kias_rent_car.data.local.entities.VehicleEntity
import edu.ucne.kias_rent_car.data.mappers.ReservacionMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.ReservacionMapper.toDomainList
import edu.ucne.kias_rent_car.data.mappers.ReservacionMapper.toEntityList
import edu.ucne.kias_rent_car.data.mappers.ReservationConfigMapper.toDomain
import edu.ucne.kias_rent_car.data.mappers.ReservationConfigMapper.toEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.ReservacionRemoteDataSource
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionRequest
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class ReservacionRepositoryImpl @Inject constructor(
    private val localDataSource: ReservacionDao,
    private val remoteDataSource: ReservacionRemoteDataSource,
    private val configDao: ReservationConfigDao,
    private val usuarioDao: UsuarioDao,
    private val vehicleDao: VehicleDao,
    private val ubicacionDao: UbicacionDao
) : ReservacionRepository {

    override suspend fun getReservaciones(): Resource<List<Reservacion>> {
        return try {
            when (val result = remoteDataSource.getReservaciones()) {
                is Resource.Success -> {
                    localDataSource.insertReservaciones(result.data.toEntityList())
                    Resource.Success(localDataSource.getReservaciones().toDomainList())
                }
                is Resource.Error -> {
                    val locales = localDataSource.getReservaciones()
                    if (locales.isNotEmpty()) {
                        Resource.Success(locales.toDomainList())
                    } else {
                        Resource.Error(result.message)
                    }
                }
                is Resource.Loading -> Resource.Loading
            }
        } catch (e: Exception) {
            val locales = localDataSource.getReservaciones()
            if (locales.isNotEmpty()) {
                Resource.Success(locales.toDomainList())
            } else {
                Resource.Error(e.localizedMessage ?: "Error al obtener reservaciones")
            }
        }
    }

    override suspend fun getReservacionesByUsuario(usuarioId: Int): Resource<List<Reservacion>> {
        return try {
            when (val result = remoteDataSource.getReservacionesByUsuario(usuarioId)) {
                is Resource.Success -> {
                    localDataSource.insertReservaciones(result.data.toEntityList())
                    Resource.Success(localDataSource.getReservacionesByUsuario(usuarioId).toDomainList())
                }
                is Resource.Error -> Resource.Success(localDataSource.getReservacionesByUsuario(usuarioId).toDomainList())
                is Resource.Loading -> Resource.Loading
            }
        } catch (e: Exception) {
            Resource.Success(localDataSource.getReservacionesByUsuario(usuarioId).toDomainList())
        }
    }

    override suspend fun getReservacionById(id: String): Resource<Reservacion> {
        val local = localDataSource.getById(id)
        if (local != null) {
            return Resource.Success(local.toDomain())
        }

        val remoteId = id.toIntOrNull()
        if (remoteId != null) {
            val byRemote = localDataSource.getByRemoteId(remoteId)
            if (byRemote != null) {
                return Resource.Success(byRemote.toDomain())
            }

            return when (val result = remoteDataSource.getReservacionById(remoteId)) {
                is Resource.Success -> Resource.Success(result.data.toDomain())
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading
            }
        }

        return Resource.Error("Reservación no encontrada")
    }

    override suspend fun createReservacionLocal(config: ReservationConfig): Resource<Reservacion> {
        val usuarioLogueado = usuarioDao.getLoggedInUsuario()
        val usuarioId = usuarioLogueado?.remoteId ?: 1
        val vehiculo = vehicleDao.getById(config.vehicleId)
        val ubicacionRecogida = ubicacionDao.getByNombre(config.lugarRecogida)
        val ubicacionDevolucion = ubicacionDao.getByNombre(config.lugarDevolucion)
        val codigoReserva = generateCodigoReserva()

        vehiculo?.let { vehicleDao.updateDisponibilidad(config.vehicleId, false) }

        val entity = buildReservacionEntity(
            params = ReservacionBuildParams(
                config = config,
                usuarioId = usuarioId,
                vehiculo = vehiculo,
                ubicacionRecogida = ubicacionRecogida,
                ubicacionDevolucion = ubicacionDevolucion,
                codigoReserva = codigoReserva,
                remoteId = null,
                isPendingCreate = true
            )
        )
        localDataSource.insertReservacion(entity)
        return Resource.Success(entity.toDomain())
    }

    override suspend fun createReservacion(config: ReservationConfig): Resource<Reservacion> {
        val usuarioLogueado = usuarioDao.getLoggedInUsuario()
        val usuarioId = usuarioLogueado?.remoteId ?: 1
        val vehiculo = vehicleDao.getById(config.vehicleId)
        val codigoReserva = generateCodigoReserva()
        val vehiculoId = vehiculo?.remoteId ?: config.vehicleId.toIntOrNull() ?: 0

        val request = ReservacionRequest(
            reservaId = 0,
            vehiculoId = vehiculoId,
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

        return when (val result = remoteDataSource.createReservacion(request)) {
            is Resource.Success -> {
                val data = result.data
                val entity = buildReservacionEntity(
                    params = ReservacionBuildParams(
                        config = config,
                        usuarioId = usuarioId,
                        vehiculo = vehiculo,
                        ubicacionRecogida = null,
                        ubicacionDevolucion = null,
                        codigoReserva = data.codigoReserva ?: codigoReserva,
                        remoteId = data.reservacionId,
                        isPendingCreate = false
                    )
                )
                localDataSource.insertReservacion(entity)
                vehiculo?.let { vehicleDao.updateDisponibilidad(config.vehicleId, false) }
                Resource.Success(entity.toDomain())
            }
            is Resource.Error -> createReservacionLocal(config)
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun updateReservacion(
        reservacionId: String,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Resource<Unit> {
        localDataSource.updateDatosLocal(
            reservacionId, ubicacionRecogidaId, ubicacionDevolucionId,
            fechaRecogida, horaRecogida, fechaDevolucion, horaDevolucion
        )

        val reservacion = localDataSource.getById(reservacionId)
        val remoteId = reservacion?.remoteId

        return if (remoteId != null) {
            when (remoteDataSource.updateReservacion(
                remoteId, ubicacionRecogidaId, ubicacionDevolucionId,
                fechaRecogida, horaRecogida, fechaDevolucion, horaDevolucion
            )) {
                is Resource.Success -> {
                    localDataSource.markAsUpdated(reservacionId)
                    Resource.Success(Unit)
                }
                else -> Resource.Success(Unit)
            }
        } else {
            Resource.Success(Unit)
        }
    }

    override suspend fun updateEstado(reservacionId: String, estado: String): Resource<Unit> {
        val reservacion = localDataSource.getById(reservacionId)
        localDataSource.updateEstadoLocal(reservacionId, estado)

        if (estado == "Cancelada" || estado == "Completada") {
            reservacion?.let {
                val vehiculo = vehicleDao.getByRemoteId(it.vehiculoId)
                vehiculo?.let { v -> vehicleDao.updateDisponibilidad(v.id, true) }
            }
        }

        val remoteId = reservacion?.remoteId
        return if (remoteId != null) {
            when (remoteDataSource.updateEstado(remoteId, estado)) {
                is Resource.Success -> {
                    localDataSource.markAsUpdated(reservacionId)
                    Resource.Success(Unit)
                }
                else -> Resource.Success(Unit)
            }
        } else {
            Resource.Success(Unit)
        }
    }

    override suspend fun cancelReservacion(reservacionId: String): Resource<Unit> {
        return updateEstado(reservacionId, "Cancelada")
    }

    override suspend fun saveReservationConfig(config: ReservationConfig): Resource<Unit> {
        configDao.clearConfig()
        configDao.saveConfig(config.toEntity())
        return Resource.Success(Unit)
    }

    override suspend fun getReservationConfig(): Resource<ReservationConfig?> {
        val config = configDao.getConfig()?.toDomain()
        return Resource.Success(config)
    }

    override suspend fun refreshReservaciones(): Resource<Unit> {
        return when (val result = remoteDataSource.getReservaciones()) {
            is Resource.Success -> {
                val hasPending = localDataSource.getPendingCreate().isNotEmpty() ||
                        localDataSource.getPendingUpdate().isNotEmpty()

                if (!hasPending) {
                    localDataSource.deleteAll()
                }
                localDataSource.insertReservaciones(result.data.toEntityList())
                Resource.Success(Unit)
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading
        }
    }

    override suspend fun postPendingReservaciones(): Resource<Unit> {
        val pendingCreate = localDataSource.getPendingCreate()
        for (reservacion in pendingCreate) {
            val request = ReservacionRequest(
                reservaId = 0,
                vehiculoId = reservacion.vehiculoId,
                usuarioId = reservacion.usuarioId.toString(),
                fechaRecogida = reservacion.fechaRecogida,
                horaRecogida = reservacion.horaRecogida,
                fechaDevolucion = reservacion.fechaDevolucion,
                horaDevolucion = reservacion.horaDevolucion,
                lugarRecogida = reservacion.ubicacionRecogidaNombre,
                lugarDevolucion = reservacion.ubicacionDevolucionNombre,
                estado = reservacion.estado,
                subtotal = reservacion.subtotal,
                impuestos = reservacion.impuestos,
                total = reservacion.total,
                codigoReserva = reservacion.codigoReserva
            )
            when (val result = remoteDataSource.createReservacion(request)) {
                is Resource.Success -> localDataSource.markAsCreated(reservacion.id, result.data.reservacionId)
                else -> Unit
            }
        }

        val pendingUpdate = localDataSource.getPendingUpdate()
        for (reservacion in pendingUpdate) {
            val remoteId = reservacion.remoteId ?: continue
            when (remoteDataSource.updateReservacion(
                remoteId, reservacion.ubicacionRecogidaId,
                reservacion.ubicacionDevolucionId, reservacion.fechaRecogida,
                reservacion.horaRecogida, reservacion.fechaDevolucion, reservacion.horaDevolucion
            )) {
                is Resource.Success -> localDataSource.markAsUpdated(reservacion.id)
                else -> Unit
            }
        }

        return Resource.Success(Unit)
    }

    private fun generateCodigoReserva(): String {
        return "KR-${System.currentTimeMillis().toString().takeLast(6)}"
    }

    private data class ReservacionBuildParams(
        val config: ReservationConfig,
        val usuarioId: Int,
        val vehiculo: VehicleEntity?,
        val ubicacionRecogida: UbicacionEntity?,
        val ubicacionDevolucion: UbicacionEntity?,
        val codigoReserva: String,
        val remoteId: Int?,
        val isPendingCreate: Boolean
    )

    private fun buildReservacionEntity(params: ReservacionBuildParams): ReservacionEntity {
        val vehiculoId = params.vehiculo?.remoteId
            ?: params.config.vehicleId.toIntOrNull()
            ?: 0

        return ReservacionEntity(
            id = UUID.randomUUID().toString(),
            remoteId = params.remoteId,
            usuarioId = params.usuarioId,
            vehiculoId = vehiculoId,
            fechaRecogida = params.config.fechaRecogida,
            horaRecogida = params.config.horaRecogida,
            fechaDevolucion = params.config.fechaDevolucion,
            horaDevolucion = params.config.horaDevolucion,
            ubicacionRecogidaId = params.ubicacionRecogida?.remoteId ?: params.config.ubicacionRecogidaId,
            ubicacionDevolucionId = params.ubicacionDevolucion?.remoteId ?: params.config.ubicacionDevolucionId,
            estado = "Confirmada",
            subtotal = params.config.subtotal,
            impuestos = params.config.impuestos,
            total = params.config.total,
            codigoReserva = params.codigoReserva,
            fechaCreacion = LocalDate.now().toString(),
            isPendingCreate = params.isPendingCreate,
            isPendingUpdate = false,
            isPendingDelete = false,
            vehiculoModelo = params.vehiculo?.modelo ?: "",
            vehiculoImagenUrl = params.vehiculo?.imagenUrl ?: "",
            vehiculoPrecioPorDia = params.vehiculo?.precioPorDia ?: 0.0,
            ubicacionRecogidaNombre = params.config.lugarRecogida,
            ubicacionDevolucionNombre = params.config.lugarDevolucion
        )
    }
}
package edu.ucne.kias_rent_car.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.local.dao.ReservationConfigDao
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.entities.ReservacionEntity
import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.local.entities.VehicleEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.ReservacionRemoteDataSource
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionDto
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReservacionRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: ReservacionRepositoryImpl
    private lateinit var localDataSource: ReservacionDao
    private lateinit var remoteDataSource: ReservacionRemoteDataSource
    private lateinit var configDao: ReservationConfigDao
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var vehicleDao: VehicleDao
    private lateinit var ubicacionDao: UbicacionDao

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        configDao = mockk(relaxed = true)
        usuarioDao = mockk(relaxed = true)
        vehicleDao = mockk(relaxed = true)
        ubicacionDao = mockk(relaxed = true)

        repository = ReservacionRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            configDao = configDao,
            usuarioDao = usuarioDao,
            vehicleDao = vehicleDao,
            ubicacionDao = ubicacionDao
        )
    }

    // ==================== getReservaciones ====================

    @Test
    fun `getReservaciones retorna datos remotos y los guarda cuando es exitoso`() = runTest {
        // Given
        val remoteDtos = listOf(createReservacionDto(1, "Confirmada", "KR-123456"))
        val localEntities = listOf(createReservacionEntity("uuid-1", 1, "Confirmada", "KR-123456"))

        coEvery { remoteDataSource.getReservaciones() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.insertReservaciones(any()) } just Runs
        coEvery { localDataSource.getReservaciones() } returns localEntities

        // When
        val result = repository.getReservaciones()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
        assertEquals("KR-123456", result.data[0].codigoReserva)
        coVerify { localDataSource.insertReservaciones(any()) }
    }

    @Test
    fun `getReservaciones retorna datos locales cuando remoto falla`() = runTest {
        // Given
        val localEntities = listOf(createReservacionEntity("uuid-1", 1, "Confirmada", "KR-123456"))

        coEvery { remoteDataSource.getReservaciones() } returns Resource.Error("Network error")
        coEvery { localDataSource.getReservaciones() } returns localEntities

        // When
        val result = repository.getReservaciones()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
    }

    @Test
    fun `getReservaciones retorna error cuando no hay datos locales ni remotos`() = runTest {
        // Given
        coEvery { remoteDataSource.getReservaciones() } returns Resource.Error("Network error")
        coEvery { localDataSource.getReservaciones() } returns emptyList()

        // When
        val result = repository.getReservaciones()

        // Then
        assertTrue(result is Resource.Error)
    }

    // ==================== getReservacionesByUsuario ====================

    @Test
    fun `getReservacionesByUsuario retorna reservaciones del usuario`() = runTest {
        // Given
        val usuarioId = 1
        val remoteDtos = listOf(createReservacionDto(1, "Confirmada", "KR-111111"))
        val localEntities = listOf(createReservacionEntity("uuid-1", 1, "Confirmada", "KR-111111"))

        coEvery { remoteDataSource.getReservacionesByUsuario(usuarioId) } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.insertReservaciones(any()) } just Runs
        coEvery { localDataSource.getReservacionesByUsuario(usuarioId) } returns localEntities

        // When
        val result = repository.getReservacionesByUsuario(usuarioId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
    }

    @Test
    fun `getReservacionesByUsuario retorna datos locales cuando remoto falla`() = runTest {
        // Given
        val usuarioId = 1
        val localEntities = listOf(createReservacionEntity("uuid-1", 1, "Confirmada", "KR-111111"))

        coEvery { remoteDataSource.getReservacionesByUsuario(usuarioId) } returns Resource.Error("Error")
        coEvery { localDataSource.getReservacionesByUsuario(usuarioId) } returns localEntities

        // When
        val result = repository.getReservacionesByUsuario(usuarioId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
    }

    // ==================== getReservacionById ====================

    @Test
    fun `getReservacionById retorna reservacion local cuando existe`() = runTest {
        // Given
        val entity = createReservacionEntity("uuid-1", 5, "Confirmada", "KR-123456")
        coEvery { localDataSource.getById("uuid-1") } returns entity

        // When
        val result = repository.getReservacionById("uuid-1")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("KR-123456", (result as Resource.Success).data.codigoReserva)
    }

    @Test
    fun `getReservacionById busca por remoteId cuando id es numerico`() = runTest {
        // Given
        val entity = createReservacionEntity("uuid-1", 5, "Confirmada", "KR-123456")
        coEvery { localDataSource.getById("5") } returns null
        coEvery { localDataSource.getByRemoteId(5) } returns entity

        // When
        val result = repository.getReservacionById("5")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("KR-123456", (result as Resource.Success).data.codigoReserva)
    }

    @Test
    fun `getReservacionById consulta remoto cuando no existe localmente`() = runTest {
        // Given
        val dto = createReservacionDto(10, "Confirmada", "KR-REMOTE")
        coEvery { localDataSource.getById("10") } returns null
        coEvery { localDataSource.getByRemoteId(10) } returns null
        coEvery { remoteDataSource.getReservacionById(10) } returns Resource.Success(dto)

        // When
        val result = repository.getReservacionById("10")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("KR-REMOTE", (result as Resource.Success).data.codigoReserva)
    }

    @Test
    fun `getReservacionById retorna error cuando no existe`() = runTest {
        // Given
        coEvery { localDataSource.getById("invalid") } returns null

        // When
        val result = repository.getReservacionById("invalid")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Reservación no encontrada", (result as Resource.Error).message)
    }

    // ==================== createReservacionLocal ====================

    @Test
    fun `createReservacionLocal guarda reservacion con isPendingCreate true`() = runTest {
        // Given
        val config = createReservationConfig()
        val usuarioEntity = createUsuarioEntity("uuid-user", 1, "Juan")
        val vehicleEntity = createVehicleEntity("uuid-vehicle", 10, "Kia K5")
        val reservacionSlot = slot<ReservacionEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { vehicleDao.getById(config.vehicleId) } returns vehicleEntity
        coEvery { ubicacionDao.getByNombre(any()) } returns null
        coEvery { vehicleDao.updateDisponibilidad(any(), any()) } just Runs
        coEvery { localDataSource.insertReservacion(capture(reservacionSlot)) } just Runs

        // When
        val result = repository.createReservacionLocal(config)

        // Then
        assertTrue(result is Resource.Success)
        assertTrue(reservacionSlot.captured.isPendingCreate)
        assertEquals("Confirmada", reservacionSlot.captured.estado)
        coVerify { vehicleDao.updateDisponibilidad(config.vehicleId, false) }
    }

    // ==================== createReservacion ====================

    @Test
    fun `createReservacion guarda con remoteId cuando servidor responde exitosamente`() = runTest {
        // Given
        val config = createReservationConfig()
        val usuarioEntity = createUsuarioEntity("uuid-user", 1, "Juan")
        val vehicleEntity = createVehicleEntity("uuid-vehicle", 10, "Kia K5")
        val responseDto = createReservacionDto(100, "Confirmada", "KR-SERVER")
        val reservacionSlot = slot<ReservacionEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { vehicleDao.getById(config.vehicleId) } returns vehicleEntity
        coEvery { remoteDataSource.createReservacion(any()) } returns Resource.Success(responseDto)
        coEvery { localDataSource.insertReservacion(capture(reservacionSlot)) } just Runs
        coEvery { vehicleDao.updateDisponibilidad(any(), any()) } just Runs

        // When
        val result = repository.createReservacion(config)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(100, reservacionSlot.captured.remoteId)
        assertFalse(reservacionSlot.captured.isPendingCreate)
    }

    @Test
    fun `createReservacion crea local cuando remoto falla`() = runTest {
        // Given
        val config = createReservationConfig()
        val usuarioEntity = createUsuarioEntity("uuid-user", 1, "Juan")
        val vehicleEntity = createVehicleEntity("uuid-vehicle", 10, "Kia K5")
        val reservacionSlot = slot<ReservacionEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { vehicleDao.getById(config.vehicleId) } returns vehicleEntity
        coEvery { ubicacionDao.getByNombre(any()) } returns null
        coEvery { remoteDataSource.createReservacion(any()) } returns Resource.Error("Sin conexión")
        coEvery { localDataSource.insertReservacion(capture(reservacionSlot)) } just Runs
        coEvery { vehicleDao.updateDisponibilidad(any(), any()) } just Runs

        // When
        val result = repository.createReservacion(config)

        // Then
        assertTrue(result is Resource.Success)
        assertTrue(reservacionSlot.captured.isPendingCreate)
    }

    // ==================== updateEstado ====================

    @Test
    fun `updateEstado actualiza localmente y sincroniza con remoto`() = runTest {
        // Given
        val reservacionId = "uuid-reservacion"
        val nuevoEstado = "Completada"
        val entity = createReservacionEntity(reservacionId, 5, "Confirmada", "KR-123456")

        coEvery { localDataSource.getById(reservacionId) } returns entity
        coEvery { localDataSource.updateEstadoLocal(reservacionId, nuevoEstado) } just Runs
        coEvery { vehicleDao.getByRemoteId(entity.vehiculoId) } returns createVehicleEntity("uuid-v", 10, "Kia")
        coEvery { vehicleDao.updateDisponibilidad(any(), any()) } just Runs
        coEvery { remoteDataSource.updateEstado(5, nuevoEstado) } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated(reservacionId) } just Runs

        // When
        val result = repository.updateEstado(reservacionId, nuevoEstado)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.updateEstadoLocal(reservacionId, nuevoEstado) }
        coVerify { localDataSource.markAsUpdated(reservacionId) }
    }

    @Test
    fun `updateEstado libera vehiculo cuando estado es Cancelada`() = runTest {
        // Given
        val reservacionId = "uuid-reservacion"
        val entity = createReservacionEntity(reservacionId, 5, "Confirmada", "KR-123456")
        val vehicleEntity = createVehicleEntity("uuid-vehicle", entity.vehiculoId, "Kia K5")

        coEvery { localDataSource.getById(reservacionId) } returns entity
        coEvery { localDataSource.updateEstadoLocal(any(), any()) } just Runs
        coEvery { vehicleDao.getByRemoteId(entity.vehiculoId) } returns vehicleEntity
        coEvery { vehicleDao.updateDisponibilidad("uuid-vehicle", true) } just Runs
        coEvery { remoteDataSource.updateEstado(any(), any()) } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated(any()) } just Runs

        // When
        repository.updateEstado(reservacionId, "Cancelada")

        // Then
        coVerify { vehicleDao.updateDisponibilidad("uuid-vehicle", true) }
    }

    @Test
    fun `updateEstado libera vehiculo cuando estado es Completada`() = runTest {
        // Given
        val reservacionId = "uuid-reservacion"
        val entity = createReservacionEntity(reservacionId, 5, "EnProceso", "KR-123456")
        val vehicleEntity = createVehicleEntity("uuid-vehicle", entity.vehiculoId, "Kia K5")

        coEvery { localDataSource.getById(reservacionId) } returns entity
        coEvery { localDataSource.updateEstadoLocal(any(), any()) } just Runs
        coEvery { vehicleDao.getByRemoteId(entity.vehiculoId) } returns vehicleEntity
        coEvery { vehicleDao.updateDisponibilidad("uuid-vehicle", true) } just Runs
        coEvery { remoteDataSource.updateEstado(any(), any()) } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated(any()) } just Runs

        // When
        repository.updateEstado(reservacionId, "Completada")

        // Then
        coVerify { vehicleDao.updateDisponibilidad("uuid-vehicle", true) }
    }

    @Test
    fun `updateEstado retorna exito aunque remoto falle`() = runTest {
        // Given
        val reservacionId = "uuid-reservacion"
        val entity = createReservacionEntity(reservacionId, 5, "Confirmada", "KR-123456")

        coEvery { localDataSource.getById(reservacionId) } returns entity
        coEvery { localDataSource.updateEstadoLocal(any(), any()) } just Runs
        coEvery { vehicleDao.getByRemoteId(any()) } returns null
        coEvery { remoteDataSource.updateEstado(5, "Cancelada") } returns Resource.Error("Sin conexión")

        // When
        val result = repository.updateEstado(reservacionId, "Cancelada")

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { localDataSource.markAsUpdated(any()) }
    }

    // ==================== cancelReservacion ====================

    @Test
    fun `cancelReservacion llama updateEstado con estado Cancelada`() = runTest {
        // Given
        val reservacionId = "uuid-reservacion"
        val entity = createReservacionEntity(reservacionId, 5, "Confirmada", "KR-123456")

        coEvery { localDataSource.getById(reservacionId) } returns entity
        coEvery { localDataSource.updateEstadoLocal(reservacionId, "Cancelada") } just Runs
        coEvery { vehicleDao.getByRemoteId(any()) } returns null
        coEvery { remoteDataSource.updateEstado(5, "Cancelada") } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated(reservacionId) } just Runs

        // When
        val result = repository.cancelReservacion(reservacionId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.updateEstadoLocal(reservacionId, "Cancelada") }
    }

    // ==================== refreshReservaciones ====================

    @Test
    fun `refreshReservaciones actualiza datos cuando no hay pendientes`() = runTest {
        // Given
        val remoteDtos = listOf(createReservacionDto(1, "Confirmada", "KR-123456"))

        coEvery { remoteDataSource.getReservaciones() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.getPendingCreate() } returns emptyList()
        coEvery { localDataSource.getPendingUpdate() } returns emptyList()
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.insertReservaciones(any()) } just Runs

        // When
        val result = repository.refreshReservaciones()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.deleteAll() }
        coVerify { localDataSource.insertReservaciones(any()) }
    }

    @Test
    fun `refreshReservaciones no elimina cuando hay pendientes`() = runTest {
        // Given
        val remoteDtos = listOf(createReservacionDto(1, "Confirmada", "KR-123456"))
        val pendingEntity = createReservacionEntity("uuid-pending", null, "Pendiente", "KR-TEMP", isPendingCreate = true)

        coEvery { remoteDataSource.getReservaciones() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingEntity)
        coEvery { localDataSource.getPendingUpdate() } returns emptyList()
        coEvery { localDataSource.insertReservaciones(any()) } just Runs

        // When
        repository.refreshReservaciones()

        // Then
        coVerify(exactly = 0) { localDataSource.deleteAll() }
    }

    // ==================== postPendingReservaciones ====================

    @Test
    fun `postPendingReservaciones sincroniza reservaciones pendientes de crear`() = runTest {
        // Given
        val pendingEntity = createReservacionEntity("uuid-pending", null, "Confirmada", "KR-TEMP", isPendingCreate = true)
        val responseDto = createReservacionDto(100, "Confirmada", "KR-100100")

        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingEntity)
        coEvery { remoteDataSource.createReservacion(any()) } returns Resource.Success(responseDto)
        coEvery { localDataSource.markAsCreated("uuid-pending", 100) } just Runs
        coEvery { localDataSource.getPendingUpdate() } returns emptyList()

        // When
        val result = repository.postPendingReservaciones()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.markAsCreated("uuid-pending", 100) }
    }

    @Test
    fun `postPendingReservaciones sincroniza reservaciones pendientes de actualizar`() = runTest {
        // Given
        val pendingEntity = createReservacionEntity("uuid-pending", 5, "Cancelada", "KR-123456", isPendingUpdate = true)

        coEvery { localDataSource.getPendingCreate() } returns emptyList()
        coEvery { localDataSource.getPendingUpdate() } returns listOf(pendingEntity)
        coEvery { remoteDataSource.updateReservacion(5, any(), any(), any(), any(), any(), any()) } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated("uuid-pending") } just Runs

        // When
        repository.postPendingReservaciones()

        // Then
        coVerify { localDataSource.markAsUpdated("uuid-pending") }
    }

    // ==================== Helper Functions ====================

    private fun createReservacionEntity(
        id: String,
        remoteId: Int?,
        estado: String,
        codigoReserva: String,
        isPendingCreate: Boolean = false,
        isPendingUpdate: Boolean = false
    ) = ReservacionEntity(
        id = id,
        remoteId = remoteId,
        usuarioId = 1,
        vehiculoId = 10,
        fechaRecogida = "2025-01-15",
        horaRecogida = "10:00",
        fechaDevolucion = "2025-01-20",
        horaDevolucion = "10:00",
        ubicacionRecogidaId = 1,
        ubicacionDevolucionId = 1,
        estado = estado,
        subtotal = 500.0,
        impuestos = 90.0,
        total = 590.0,
        codigoReserva = codigoReserva,
        fechaCreacion = "2025-01-10",
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = false,
        vehiculoModelo = "Kia K5",
        vehiculoImagenUrl = "url",
        vehiculoPrecioPorDia = 100.0,
        ubicacionRecogidaNombre = "Aeropuerto",
        ubicacionDevolucionNombre = "Aeropuerto"
    )

    private fun createReservacionDto(
        id: Int,
        estado: String,
        codigoReserva: String
    ) = ReservacionDto(
        reservacionId = id,
        usuarioId = 1,
        vehiculoId = 10,
        fechaRecogida = "2025-01-15",
        horaRecogida = "10:00",
        fechaDevolucion = "2025-01-20",
        horaDevolucion = "10:00",
        ubicacionRecogidaId = 1,
        ubicacionDevolucionId = 1,
        estado = estado,
        subtotal = 500.0,
        impuestos = 90.0,
        total = 590.0,
        codigoReserva = codigoReserva,
        fechaCreacion = "2025-01-10",
        usuario = null,
        vehiculo = null,
        ubicacionRecogida = null,
        ubicacionDevolucion = null
    )

    private fun createReservationConfig() = ReservationConfig(
        vehicleId = "uuid-vehicle",
        fechaRecogida = "2025-01-15",
        horaRecogida = "10:00",
        fechaDevolucion = "2025-01-20",
        horaDevolucion = "10:00",
        lugarRecogida = "Aeropuerto",
        lugarDevolucion = "Aeropuerto",
        ubicacionRecogidaId = 1,
        ubicacionDevolucionId = 1,
        dias = 5,
        subtotal = 500.0,
        impuestos = 90.0,
        total = 590.0
    )

    private fun createUsuarioEntity(
        id: String,
        remoteId: Int,
        nombre: String
    ) = UsuarioEntity(
        id = id,
        remoteId = remoteId,
        nombre = nombre,
        email = "test@email.com",
        password = "pass",
        telefono = null,
        rol = "Cliente",
        isLoggedIn = true
    )

    private fun createVehicleEntity(
        id: String,
        remoteId: Int,
        modelo: String
    ) = VehicleEntity(
        id = id,
        remoteId = remoteId,
        modelo = modelo,
        descripcion = "Descripción",
        categoria = "Sedan",
        asientos = 5,
        transmision = "Automático",
        precioPorDia = 100.0,
        imagenUrl = "url",
        disponible = true,
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false
    )
}
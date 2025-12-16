package edu.ucne.kias_rent_car.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.entities.MensajeEntity
import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.MensajeRemoteDataSource
import edu.ucne.kias_rent_car.data.remote.dto.MensajeDto
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MensajeRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: MensajeRepositoryImpl
    private lateinit var localDataSource: MensajeDao
    private lateinit var remoteDataSource: MensajeRemoteDataSource
    private lateinit var usuarioDao: UsuarioDao

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        usuarioDao = mockk(relaxed = true)

        repository = MensajeRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            usuarioDao = usuarioDao
        )
    }

    // ==================== getMensajes ====================

    @Test
    fun `getMensajes retorna datos remotos y los guarda cuando es exitoso`() = runTest {
        // Given
        val remoteDtos = listOf(
            MensajeDto(
                mensajeId = 1,
                usuarioId = 1,
                nombreUsuario = "Juan",
                asunto = "Test",
                contenido = "Contenido",
                respuesta = null,
                fechaCreacion = "2025-01-10",
                leido = false
            )
        )
        val localEntities = listOf(
            createMensajeEntity("uuid-1", 1, "Test", "Contenido")
        )

        coEvery { remoteDataSource.getMensajes() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.getPendingCreate() } returns emptyList()
        coEvery { localDataSource.getPendingUpdate() } returns emptyList()
        coEvery { localDataSource.insertMensajes(any()) } just Runs
        coEvery { localDataSource.getMensajes() } returns localEntities

        // When
        val result = repository.getMensajes()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
        coVerify { localDataSource.insertMensajes(any()) }
    }

    @Test
    fun `getMensajes retorna datos locales cuando remoto falla`() = runTest {
        // Given
        val localEntities = listOf(
            createMensajeEntity("uuid-1", 1, "Test", "Contenido")
        )

        coEvery { remoteDataSource.getMensajes() } returns Resource.Error("Network error")
        coEvery { localDataSource.getMensajes() } returns localEntities

        // When
        val result = repository.getMensajes()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
    }

    @Test
    fun `getMensajes retorna error cuando no hay datos locales ni remotos`() = runTest {
        // Given
        coEvery { remoteDataSource.getMensajes() } returns Resource.Error("Network error")
        coEvery { localDataSource.getMensajes() } returns emptyList()

        // When
        val result = repository.getMensajes()

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `getMensajes no sobreescribe mensajes pendientes`() = runTest {
        // Given
        val pendingEntity = createMensajeEntity("uuid-pending", 5, "Pendiente", "Contenido", isPendingCreate = true)
        val remoteDtos = listOf(
            MensajeDto(mensajeId = 5, usuarioId = 1, nombreUsuario = "Juan", asunto = "Remote", contenido = "C", respuesta = null, fechaCreacion = "2025-01-10", leido = false)
        )

        coEvery { remoteDataSource.getMensajes() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingEntity)
        coEvery { localDataSource.getPendingUpdate() } returns emptyList()
        coEvery { localDataSource.insertMensajes(any()) } just Runs
        coEvery { localDataSource.getMensajes() } returns listOf(pendingEntity)

        // When
        repository.getMensajes()

        // Then
        coVerify {
            localDataSource.insertMensajes(match { list ->
                list.none { it.remoteId == 5 }
            })
        }
    }

    // ==================== getMensajeById ====================

    @Test
    fun `getMensajeById retorna mensaje local cuando existe`() = runTest {
        // Given
        val entity = createMensajeEntity("uuid-1", 1, "Test", "Contenido")
        coEvery { localDataSource.getById("uuid-1") } returns entity

        // When
        val result = repository.getMensajeById("uuid-1")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Test", (result as Resource.Success).data.asunto)
    }

    @Test
    fun `getMensajeById busca por remoteId cuando id es numerico`() = runTest {
        // Given
        val entity = createMensajeEntity("uuid-1", 5, "Test", "Contenido")
        coEvery { localDataSource.getById("5") } returns null
        coEvery { localDataSource.getByRemoteId(5) } returns entity

        // When
        val result = repository.getMensajeById("5")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Test", (result as Resource.Success).data.asunto)
    }

    @Test
    fun `getMensajeById consulta remoto cuando no existe localmente`() = runTest {
        // Given
        val dto = MensajeDto(mensajeId = 10, usuarioId = 1, nombreUsuario = "Juan", asunto = "Remoto", contenido = "C", respuesta = null, fechaCreacion = "2025-01-10", leido = false)
        coEvery { localDataSource.getById("10") } returns null
        coEvery { localDataSource.getByRemoteId(10) } returns null
        coEvery { remoteDataSource.getMensajeById(10) } returns Resource.Success(dto)

        // When
        val result = repository.getMensajeById("10")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Remoto", (result as Resource.Success).data.asunto)
    }

    @Test
    fun `getMensajeById retorna error cuando no existe`() = runTest {
        // Given
        coEvery { localDataSource.getById("invalid") } returns null

        // When
        val result = repository.getMensajeById("invalid")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Mensaje no encontrado", (result as Resource.Error).message)
    }

    // ==================== getMensajesByUsuario ====================

    @Test
    fun `getMensajesByUsuario retorna mensajes del usuario`() = runTest {
        // Given
        val usuarioId = 1
        val remoteDtos = listOf(
            MensajeDto(mensajeId = 1, usuarioId = usuarioId, nombreUsuario = "Juan", asunto = "Test", contenido = "C", respuesta = null, fechaCreacion = "2025-01-10", leido = false)
        )
        val localEntities = listOf(
            createMensajeEntity("uuid-1", 1, "Test", "Contenido")
        )

        coEvery { remoteDataSource.getMensajesByUsuario(usuarioId) } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.insertMensajes(any()) } just Runs
        coEvery { localDataSource.getMensajesByUsuario(usuarioId) } returns localEntities

        // When
        val result = repository.getMensajesByUsuario(usuarioId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
    }

    // ==================== createMensajeLocal ====================

    @Test
    fun `createMensajeLocal guarda mensaje con isPendingCreate true`() = runTest {
        // Given
        val usuarioEntity = createUsuarioEntity("uuid-user", 1, "Juan")
        val mensajeSlot = slot<MensajeEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { localDataSource.insertMensaje(capture(mensajeSlot)) } just Runs

        // When
        val result = repository.createMensajeLocal("Asunto", "Contenido")

        // Then
        assertTrue(result is Resource.Success)
        assertTrue(mensajeSlot.captured.isPendingCreate)
        assertEquals("Asunto", mensajeSlot.captured.asunto)
        assertEquals(1, mensajeSlot.captured.usuarioId)
    }

    @Test
    fun `createMensajeLocal usa valores por defecto cuando no hay usuario logueado`() = runTest {
        // Given
        val mensajeSlot = slot<MensajeEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns null
        coEvery { localDataSource.insertMensaje(capture(mensajeSlot)) } just Runs

        // When
        repository.createMensajeLocal("Asunto", "Contenido")

        // Then
        assertEquals(1, mensajeSlot.captured.usuarioId)
        assertEquals("Usuario", mensajeSlot.captured.nombreUsuario)
    }

    // ==================== sendMensaje ====================

    @Test
    fun `sendMensaje guarda con remoteId cuando servidor responde exitosamente`() = runTest {
        // Given
        val usuarioEntity = createUsuarioEntity("uuid-user", 1, "Juan")
        val responseDto = MensajeDto(
            mensajeId = 100,
            usuarioId = 1,
            nombreUsuario = "Juan",
            asunto = "Test",
            contenido = "Contenido",
            respuesta = null,
            fechaCreacion = "2025-01-10",
            leido = false
        )
        val mensajeSlot = slot<MensajeEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { remoteDataSource.sendMensaje(1, "Test", "Contenido") } returns Resource.Success(responseDto)
        coEvery { localDataSource.insertMensaje(capture(mensajeSlot)) } just Runs

        // When
        val result = repository.sendMensaje("Test", "Contenido")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(100, mensajeSlot.captured.remoteId)
        assertFalse(mensajeSlot.captured.isPendingCreate)
    }

    @Test
    fun `sendMensaje crea mensaje local cuando remoto falla`() = runTest {
        // Given
        val usuarioEntity = createUsuarioEntity("uuid-user", 1, "Juan")
        val mensajeSlot = slot<MensajeEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { remoteDataSource.sendMensaje(1, "Test", "Contenido") } returns Resource.Error("Sin conexión")
        coEvery { localDataSource.insertMensaje(capture(mensajeSlot)) } just Runs

        // When
        val result = repository.sendMensaje("Test", "Contenido")

        // Then
        assertTrue(result is Resource.Success)
        assertTrue(mensajeSlot.captured.isPendingCreate)
        assertNull(mensajeSlot.captured.remoteId)
    }

    // ==================== responderMensaje ====================

    @Test
    fun `responderMensaje actualiza localmente y sincroniza con remoto`() = runTest {
        // Given
        val mensajeId = "uuid-mensaje"
        val respuesta = "Gracias por su consulta"
        val entity = createMensajeEntity(mensajeId, 5, "Test", "Contenido")

        coEvery { localDataSource.updateRespuestaLocal(mensajeId, respuesta) } just Runs
        coEvery { localDataSource.getById(mensajeId) } returns entity
        coEvery { remoteDataSource.responderMensaje(5, respuesta) } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated(mensajeId) } just Runs

        // When
        val result = repository.responderMensaje(mensajeId, respuesta)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.updateRespuestaLocal(mensajeId, respuesta) }
        coVerify { localDataSource.markAsUpdated(mensajeId) }
    }

    @Test
    fun `responderMensaje retorna exito aunque remoto falle`() = runTest {
        // Given
        val mensajeId = "uuid-mensaje"
        val respuesta = "Respuesta"
        val entity = createMensajeEntity(mensajeId, 5, "Test", "Contenido")

        coEvery { localDataSource.updateRespuestaLocal(mensajeId, respuesta) } just Runs
        coEvery { localDataSource.getById(mensajeId) } returns entity
        coEvery { remoteDataSource.responderMensaje(5, respuesta) } returns Resource.Error("Sin conexión")

        // When
        val result = repository.responderMensaje(mensajeId, respuesta)

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { localDataSource.markAsUpdated(any()) }
    }

    @Test
    fun `responderMensaje funciona sin remoteId`() = runTest {
        // Given
        val mensajeId = "uuid-local"
        val respuesta = "Respuesta"
        val entity = createMensajeEntity(mensajeId, null, "Test", "Contenido")

        coEvery { localDataSource.updateRespuestaLocal(mensajeId, respuesta) } just Runs
        coEvery { localDataSource.getById(mensajeId) } returns entity

        // When
        val result = repository.responderMensaje(mensajeId, respuesta)

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { remoteDataSource.responderMensaje(any(), any()) }
    }

    // ==================== postPendingMensajes ====================

    @Test
    fun `postPendingMensajes sincroniza mensajes pendientes de crear`() = runTest {
        // Given
        val pendingEntity = createMensajeEntity("uuid-pending", null, "Pendiente", "Contenido", isPendingCreate = true)
        val responseDto = MensajeDto(mensajeId = 200, usuarioId = 1, nombreUsuario = "Juan", asunto = "Pendiente", contenido = "Contenido", respuesta = null, fechaCreacion = "2025-01-10", leido = false)

        coEvery { localDataSource.getPendingCreate() } returns listOf(pendingEntity)
        coEvery { remoteDataSource.sendMensaje(1, "Pendiente", "Contenido") } returns Resource.Success(responseDto)
        coEvery { localDataSource.markAsCreated("uuid-pending", 200) } just Runs
        coEvery { localDataSource.getPendingUpdate() } returns emptyList()

        // When
        val result = repository.postPendingMensajes()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.markAsCreated("uuid-pending", 200) }
    }

    @Test
    fun `postPendingMensajes sincroniza mensajes pendientes de actualizar`() = runTest {
        // Given
        val pendingEntity = createMensajeEntity("uuid-pending", 5, "Test", "Contenido", isPendingUpdate = true)
            .copy(respuesta = "Respuesta pendiente")

        coEvery { localDataSource.getPendingCreate() } returns emptyList()
        coEvery { localDataSource.getPendingUpdate() } returns listOf(pendingEntity)
        coEvery { remoteDataSource.responderMensaje(5, "Respuesta pendiente") } returns Resource.Success(Unit)
        coEvery { localDataSource.markAsUpdated("uuid-pending") } just Runs

        // When
        repository.postPendingMensajes()

        // Then
        coVerify { localDataSource.markAsUpdated("uuid-pending") }
    }

    // ==================== Helper Functions ====================
    private fun createMensajeEntity(
        id: String,
        remoteId: Int?,
        asunto: String,
        contenido: String,
        isPendingCreate: Boolean = false,
        isPendingUpdate: Boolean = false
    ) = MensajeEntity(
        id = id,
        remoteId = remoteId,
        usuarioId = 1,
        nombreUsuario = "Juan",
        asunto = asunto,
        contenido = contenido,
        respuesta = null,
        fechaCreacion = "2025-01-10",
        leido = false,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate
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
}
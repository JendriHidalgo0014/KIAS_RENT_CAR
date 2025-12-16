package edu.ucne.kias_rent_car.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.UsuarioRemoteDataSource
import edu.ucne.kias_rent_car.data.remote.dto.UsuarioDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UsuarioRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: UsuarioRepositoryImpl
    private lateinit var localDataSource: UsuarioDao
    private lateinit var remoteDataSource: UsuarioRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = UsuarioRepositoryImpl(localDataSource, remoteDataSource)
    }

    // ==================== login ====================

    @Test
    fun `login retorna error cuando email esta vacio`() = runTest {
        // Given
        val email = ""
        val password = "password123"

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email y contraseña son requeridos", (result as Resource.Error).message)
        coVerify(exactly = 0) { remoteDataSource.login(any(), any()) }
    }

    @Test
    fun `login retorna error cuando password esta vacio`() = runTest {
        // Given
        val email = "test@email.com"
        val password = "   "

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email y contraseña son requeridos", (result as Resource.Error).message)
    }

    @Test
    fun `login exitoso guarda usuario con isLoggedIn true`() = runTest {
        // Given
        val email = "test@email.com"
        val password = "password123"
        val usuarioDto = UsuarioDto(
            usuarioId = 1,
            nombre = "Test User",
            email = email,
            password = password,
            telefono = "809-555-1234",
            rol = "Cliente"
        )
        val entitySlot = slot<UsuarioEntity>()

        coEvery { remoteDataSource.login(email, password) } returns Resource.Success(usuarioDto)
        coEvery { localDataSource.logoutAll() } just Runs
        coEvery { localDataSource.insertUsuario(capture(entitySlot)) } just Runs

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Test User", (result as Resource.Success).data.nombre)
        assertTrue(entitySlot.captured.isLoggedIn)
        coVerify { localDataSource.logoutAll() }
    }

    @Test
    fun `login usa datos locales cuando falla conexion remota`() = runTest {
        // Given
        val email = "local@email.com"
        val password = "localpass"
        val localEntity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 1,
            nombre = "Local User",
            email = email,
            password = password,
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )

        coEvery { remoteDataSource.login(email, password) } returns Resource.Error("Sin conexión")
        coEvery { localDataSource.getByEmail(email) } returns localEntity
        coEvery { localDataSource.logoutAll() } just Runs
        coEvery { localDataSource.setLoggedIn("uuid-1") } just Runs

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Local User", (result as Resource.Success).data.nombre)
        coVerify { localDataSource.setLoggedIn("uuid-1") }
    }

    @Test
    fun `login local falla con credenciales incorrectas`() = runTest {
        // Given
        val email = "test@email.com"
        val password = "wrongpass"
        val localEntity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 1,
            nombre = "User",
            email = email,
            password = "correctpass",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )

        coEvery { remoteDataSource.login(email, password) } returns Resource.Error("Sin conexión")
        coEvery { localDataSource.getByEmail(email) } returns localEntity

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email o contraseña incorrectos", (result as Resource.Error).message)
    }

    @Test
    fun `login trimea el email antes de procesar`() = runTest {
        // Given
        val email = "  test@email.com  "
        val password = "password"
        val usuarioDto = UsuarioDto(
            usuarioId = 1,
            nombre = "User",
            email = "test@email.com",
            password = password,
            telefono = null,
            rol = "Cliente"
        )

        coEvery { remoteDataSource.login("test@email.com", password) } returns Resource.Success(usuarioDto)
        coEvery { localDataSource.logoutAll() } just Runs
        coEvery { localDataSource.insertUsuario(any()) } just Runs

        // When
        repository.login(email, password)

        // Then
        coVerify { remoteDataSource.login("test@email.com", password) }
    }

    // ==================== registrarUsuario ====================

    @Test
    fun `registrarUsuario retorna error cuando nombre esta vacio`() = runTest {
        // When
        val result = repository.registrarUsuario("", "email@test.com", "pass123", null)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Todos los campos son requeridos", (result as Resource.Error).message)
    }

    @Test
    fun `registrarUsuario retorna error cuando password es muy corta`() = runTest {
        // When
        val result = repository.registrarUsuario("Nombre", "email@test.com", "123", null)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("La contraseña debe tener al menos 4 caracteres", (result as Resource.Error).message)
    }

    @Test
    fun `registrarUsuario exitoso guarda usuario logueado`() = runTest {
        // Given
        val usuarioDto = UsuarioDto(
            usuarioId = 1,
            nombre = "Nuevo Usuario",
            email = "nuevo@email.com",
            password = "pass1234",
            telefono = "809-555-0000",
            rol = "Cliente"
        )
        val entitySlot = slot<UsuarioEntity>()

        coEvery {
            remoteDataSource.registro("Nuevo Usuario", "nuevo@email.com", "pass1234", "809-555-0000")
        } returns Resource.Success(usuarioDto)
        coEvery { localDataSource.logoutAll() } just Runs
        coEvery { localDataSource.insertUsuario(capture(entitySlot)) } just Runs

        // When
        val result = repository.registrarUsuario("Nuevo Usuario", "nuevo@email.com", "pass1234", "809-555-0000")

        // Then
        assertTrue(result is Resource.Success)
        assertTrue(entitySlot.captured.isLoggedIn)
        coVerify { localDataSource.logoutAll() }
    }

    @Test
    fun `registrarUsuario propaga error del remoto`() = runTest {
        // Given
        coEvery {
            remoteDataSource.registro(any(), any(), any(), any())
        } returns Resource.Error("Email ya registrado")

        // When
        val result = repository.registrarUsuario("User", "existe@email.com", "pass1234", null)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email ya registrado", (result as Resource.Error).message)
    }

    // ==================== logout ====================

    @Test
    fun `logout llama logoutAll en dao`() = runTest {
        // Given
        coEvery { localDataSource.logoutAll() } just Runs

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.logoutAll() }
    }

    // ==================== getUsuarioLogueado ====================

    @Test
    fun `getUsuarioLogueado retorna usuario cuando existe`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 1,
            nombre = "Logueado",
            email = "log@email.com",
            password = "pass",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = true
        )
        coEvery { localDataSource.getLoggedInUsuario() } returns entity

        // When
        val result = repository.getUsuarioLogueado()

        // Then
        assertTrue(result is Resource.Success)
        assertNotNull((result as Resource.Success).data)
        assertEquals("Logueado", result.data?.nombre)
    }

    @Test
    fun `getUsuarioLogueado retorna null cuando no hay usuario`() = runTest {
        // Given
        coEvery { localDataSource.getLoggedInUsuario() } returns null

        // When
        val result = repository.getUsuarioLogueado()

        // Then
        assertTrue(result is Resource.Success)
        assertNull((result as Resource.Success).data)
    }

    // ==================== observeUsuarioLogueado ====================

    @Test
    fun `observeUsuarioLogueado emite cambios del dao`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 1,
            nombre = "Observable",
            email = "obs@email.com",
            password = "pass",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = true
        )
        coEvery { localDataSource.observeLoggedInUsuario() } returns flowOf(entity)

        // When
        val result = repository.observeUsuarioLogueado().first()

        // Then
        assertNotNull(result)
        assertEquals("Observable", result?.nombre)
    }

    // ==================== getUsuarioById ====================

    @Test
    fun `getUsuarioById retorna local cuando existe por id`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 1,
            nombre = "Local User",
            email = "local@email.com",
            password = "pass",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )
        coEvery { localDataSource.getById("uuid-1") } returns entity

        // When
        val result = repository.getUsuarioById("uuid-1")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Local User", (result as Resource.Success).data.nombre)
    }

    @Test
    fun `getUsuarioById busca por remoteId cuando id es numerico`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 5,
            nombre = "By Remote",
            email = "remote@email.com",
            password = "pass",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )
        coEvery { localDataSource.getById("5") } returns null
        coEvery { localDataSource.getByRemoteId(5) } returns entity

        // When
        val result = repository.getUsuarioById("5")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("By Remote", (result as Resource.Success).data.nombre)
    }

    @Test
    fun `getUsuarioById consulta remoto y guarda localmente`() = runTest {
        // Given
        val usuarioDto = UsuarioDto(
            usuarioId = 10,
            nombre = "Remote User",
            email = "rem@email.com",
            password = "pass",
            telefono = null,
            rol = "Cliente"
        )
        coEvery { localDataSource.getById("10") } returns null
        coEvery { localDataSource.getByRemoteId(10) } returns null
        coEvery { remoteDataSource.getUsuarioById(10) } returns Resource.Success(usuarioDto)
        coEvery { localDataSource.insertUsuario(any()) } just Runs

        // When
        val result = repository.getUsuarioById("10")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Remote User", (result as Resource.Success).data.nombre)
        coVerify { localDataSource.insertUsuario(any()) }
    }

    @Test
    fun `getUsuarioById retorna error cuando id no es valido`() = runTest {
        // Given
        coEvery { localDataSource.getById("invalid-uuid") } returns null

        // When
        val result = repository.getUsuarioById("invalid-uuid")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("ID inválido", (result as Resource.Error).message)
    }

    // ==================== getAllUsuarios ====================

    @Test
    fun `getAllUsuarios retorna datos remotos y los guarda`() = runTest {
        // Given
        val dtos = listOf(
            UsuarioDto(usuarioId = 1, nombre = "User1", email = "u1@email.com", password = "p", telefono = null, rol = "Cliente"),
            UsuarioDto(usuarioId = 2, nombre = "User2", email = "u2@email.com", password = "p", telefono = null, rol = "Admin")
        )
        coEvery { remoteDataSource.getUsuarios() } returns Resource.Success(dtos)
        coEvery { localDataSource.insertUsuario(any()) } just Runs

        // When
        val result = repository.getAllUsuarios()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data.size)
        coVerify(exactly = 2) { localDataSource.insertUsuario(any()) }
    }

    @Test
    fun `getAllUsuarios retorna datos locales cuando falla remoto`() = runTest {
        // Given
        val localEntities = listOf(
            UsuarioEntity(id = "1", remoteId = 1, nombre = "Local1", email = "l@e.com", password = "p", telefono = null, rol = "Cliente", isLoggedIn = false)
        )
        coEvery { remoteDataSource.getUsuarios() } returns Resource.Error("Sin conexión")
        coEvery { localDataSource.getAllUsuarios() } returns localEntities

        // When
        val result = repository.getAllUsuarios()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
    }

    @Test
    fun `getAllUsuarios retorna error cuando no hay datos locales ni remotos`() = runTest {
        // Given
        coEvery { remoteDataSource.getUsuarios() } returns Resource.Error("Error de red")
        coEvery { localDataSource.getAllUsuarios() } returns emptyList()

        // When
        val result = repository.getAllUsuarios()

        // Then
        assertTrue(result is Resource.Error)
    }

    // ==================== deleteUsuario ====================

    @Test
    fun `deleteUsuario elimina usando remoteId de entidad local`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 5,
            nombre = "ToDelete",
            email = "del@email.com",
            password = "p",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )
        coEvery { localDataSource.getById("uuid-1") } returns entity
        coEvery { remoteDataSource.deleteUsuario(5) } returns Resource.Success(Unit)
        coEvery { localDataSource.deleteById("uuid-1") } just Runs

        // When
        val result = repository.deleteUsuario("uuid-1")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deleteUsuario(5) }
        coVerify { localDataSource.deleteById("uuid-1") }
    }

    @Test
    fun `deleteUsuario usa id numerico como remoteId`() = runTest {
        // Given
        coEvery { localDataSource.getById("10") } returns null
        coEvery { remoteDataSource.deleteUsuario(10) } returns Resource.Success(Unit)
        coEvery { localDataSource.deleteById("10") } just Runs

        // When
        val result = repository.deleteUsuario("10")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deleteUsuario(10) }
    }

    @Test
    fun `deleteUsuario elimina solo local cuando no hay remoteId`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "local-only",
            remoteId = null,
            nombre = "Local Only",
            email = "lo@email.com",
            password = "p",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )
        coEvery { localDataSource.getById("local-only") } returns entity
        coEvery { localDataSource.deleteById("local-only") } just Runs

        // When
        val result = repository.deleteUsuario("local-only")

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { remoteDataSource.deleteUsuario(any()) }
        coVerify { localDataSource.deleteById("local-only") }
    }

    @Test
    fun `deleteUsuario propaga error del remoto`() = runTest {
        // Given
        val entity = UsuarioEntity(
            id = "uuid-1",
            remoteId = 5,
            nombre = "User",
            email = "u@email.com",
            password = "p",
            telefono = null,
            rol = "Cliente",
            isLoggedIn = false
        )
        coEvery { localDataSource.getById("uuid-1") } returns entity
        coEvery { remoteDataSource.deleteUsuario(5) } returns Resource.Error("No autorizado")

        // When
        val result = repository.deleteUsuario("uuid-1")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No autorizado", (result as Resource.Error).message)
        coVerify(exactly = 0) { localDataSource.deleteById(any()) }
    }
}
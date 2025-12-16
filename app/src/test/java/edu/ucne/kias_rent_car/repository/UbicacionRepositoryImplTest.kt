package edu.ucne.kias_rent_car.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.entities.UbicacionEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.UbicacionRemoteDataSource
import edu.ucne.kias_rent_car.data.remote.dto.UbicacionDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UbicacionRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: UbicacionRepositoryImpl
    private lateinit var localDataSource: UbicacionDao
    private lateinit var remoteDataSource: UbicacionRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = UbicacionRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getUbicaciones retorna datos remotos y los guarda localmente cuando es exitoso`() = runTest {
        // Given
        val remoteDtos = listOf(
            UbicacionDto(ubicacionId = 1, nombre = "Aeropuerto SDQ", direccion = "Santo Domingo"),
            UbicacionDto(ubicacionId = 2, nombre = "Aeropuerto STI", direccion = "Santiago")
        )
        val localEntities = listOf(
            UbicacionEntity(id = "1", remoteId = 1, nombre = "Aeropuerto SDQ", direccion = "Santo Domingo"),
            UbicacionEntity(id = "2", remoteId = 2, nombre = "Aeropuerto STI", direccion = "Santiago")
        )

        coEvery { remoteDataSource.getUbicaciones() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.insertUbicaciones(any()) } just Runs
        coEvery { localDataSource.getUbicaciones() } returns localEntities

        val result = repository.getUbicaciones()

        assertEquals(2, result.size)
        assertEquals("Aeropuerto SDQ", result[0].nombre)
        coVerify { localDataSource.insertUbicaciones(any()) }
    }

    @Test
    fun `getUbicaciones retorna datos locales cuando falla la conexion remota`() = runTest {
        val localEntities = listOf(
            UbicacionEntity(id = "1", remoteId = 1, nombre = "Ubicacion Local", direccion = "Dir")
        )

        coEvery { remoteDataSource.getUbicaciones() } returns Resource.Error("Sin conexión")
        coEvery { localDataSource.getUbicaciones() } returns localEntities

        val result = repository.getUbicaciones()
        assertEquals(1, result.size)
        assertEquals("Ubicacion Local", result[0].nombre)
        coVerify(exactly = 0) { localDataSource.insertUbicaciones(any()) }
    }
    @Test
    fun `getUbicaciones retorna datos locales cuando hay excepcion`() = runTest {
        val localEntities = listOf(
            UbicacionEntity(id = "1", remoteId = 1, nombre = "Fallback", direccion = "Dir")
        )

        coEvery { remoteDataSource.getUbicaciones() } throws RuntimeException("Network error")
        coEvery { localDataSource.getUbicaciones() } returns localEntities

        val result = repository.getUbicaciones()

        assertEquals(1, result.size)
        assertEquals("Fallback", result[0].nombre)
    }
    @Test
    fun `observeUbicaciones emite lista de ubicaciones del dao`() = runTest {
        val entities = listOf(
            UbicacionEntity(id = "1", remoteId = 1, nombre = "Ubicacion 1", direccion = "Dir 1")
        )
        coEvery { localDataSource.observeUbicaciones() } returns flowOf(entities)

        val result = repository.observeUbicaciones().first()

        assertEquals(1, result.size)
        assertEquals("Ubicacion 1", result[0].nombre)
    }
    @Test
    fun `getUbicacionById retorna ubicacion local cuando existe por id`() = runTest {
        val entity = UbicacionEntity(id = "uuid-1", remoteId = 1, nombre = "Local", direccion = "Dir")
        coEvery { localDataSource.getById("uuid-1") } returns entity

        val result = repository.getUbicacionById("uuid-1")

        assertNotNull(result)
        assertEquals("Local", result?.nombre)
        coVerify(exactly = 0) { remoteDataSource.getUbicacionById(any()) }
    }

    @Test
    fun `getUbicacionById busca por remoteId cuando id es numerico`() = runTest {
        val entity =
            UbicacionEntity(id = "uuid-1", remoteId = 5, nombre = "Por Remote", direccion = "Dir")
        coEvery { localDataSource.getById("5") } returns null
        coEvery { localDataSource.getByRemoteId(5) } returns entity

        val result = repository.getUbicacionById("5")

        assertNotNull(result)
        assertEquals("Por Remote", result?.nombre)
    }

    @Test
    fun `getUbicacionById consulta remoto cuando no existe localmente`() = runTest {
        val remoteDto = UbicacionDto(ubicacionId = 10, nombre = "Remoto", direccion = "Dir Remota")
        coEvery { localDataSource.getById("10") } returns null
        coEvery { localDataSource.getByRemoteId(10) } returns null
        coEvery { remoteDataSource.getUbicacionById(10) } returns Resource.Success(remoteDto)

        val result = repository.getUbicacionById("10")

        assertNotNull(result)
        assertEquals("Remoto", result?.nombre)
    }

    @Test
    fun `getUbicacionById retorna null cuando no existe en ninguna fuente`() = runTest {

        coEvery { localDataSource.getById("99") } returns null
        coEvery { localDataSource.getByRemoteId(99) } returns null
        coEvery { remoteDataSource.getUbicacionById(99) } returns Resource.Error("No encontrado")

        val result = repository.getUbicacionById("99")

        assertNull(result)
    }

    @Test
    fun `getUbicacionById retorna null cuando id no es numerico y no existe local`() = runTest {

        coEvery { localDataSource.getById("abc-invalid") } returns null

        val result = repository.getUbicacionById("abc-invalid")

        assertNull(result)
        coVerify(exactly = 0) { remoteDataSource.getUbicacionById(any()) }
    }

    @Test
    fun `refreshUbicaciones limpia y actualiza datos locales cuando es exitoso`() = runTest {

        val remoteDtos = listOf(
            UbicacionDto(ubicacionId = 1, nombre = "Nueva", direccion = "Dir")
        )
        coEvery { remoteDataSource.getUbicaciones() } returns Resource.Success(remoteDtos)
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.insertUbicaciones(any()) } just Runs

        repository.refreshUbicaciones()

        coVerify { localDataSource.deleteAll() }
        coVerify { localDataSource.insertUbicaciones(any()) }
    }

    @Test
    fun `refreshUbicaciones no modifica datos locales cuando falla`() = runTest {

        coEvery { remoteDataSource.getUbicaciones() } returns Resource.Error("Error de red")

        repository.refreshUbicaciones()

        coVerify(exactly = 0) { localDataSource.deleteAll() }
        coVerify(exactly = 0) { localDataSource.insertUbicaciones(any()) }
    }
}
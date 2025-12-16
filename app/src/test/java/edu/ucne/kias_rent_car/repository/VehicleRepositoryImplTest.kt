package edu.ucne.kias_rent_car.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.entities.VehicleEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.VehiculoRemoteDataSource
import edu.ucne.kias_rent_car.data.remote.dto.VehiculoDto
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.model.VehicleParams
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VehicleRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: VehicleRepositoryImpl
    private lateinit var localDataSource: VehicleDao
    private lateinit var remoteDataSource: VehiculoRemoteDataSource

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        repository = VehicleRepositoryImpl(localDataSource, remoteDataSource)
    }

    // ==================== observeAvailableVehicles ====================

    @Test
    fun `observeAvailableVehicles emite lista de vehiculos del dao`() = runTest {
        // Given
        val entities = listOf(
            createVehicleEntity("1", 1, "Kia Sportage", "SUV"),
            createVehicleEntity("2", 2, "Kia Rio", "Sedan")
        )
        coEvery { localDataSource.observeAllVehicles() } returns flowOf(entities)

        // When
        val result = repository.observeAvailableVehicles().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Kia Sportage", result[0].modelo)
    }

    // ==================== observeVehiclesByCategory ====================

    @Test
    fun `observeVehiclesByCategory con ALL retorna todos los vehiculos`() = runTest {
        // Given
        val entities = listOf(
            createVehicleEntity("1", 1, "Kia Sportage", "SUV"),
            createVehicleEntity("2", 2, "Kia Rio", "Sedan")
        )
        coEvery { localDataSource.observeAllVehicles() } returns flowOf(entities)

        // When
        val result = repository.observeVehiclesByCategory(VehicleCategory.ALL).first()

        // Then
        assertEquals(2, result.size)
        coVerify(exactly = 0) { localDataSource.observeVehiclesByCategory(any()) }
    }

    @Test
    fun `observeVehiclesByCategory filtra por categoria especifica`() = runTest {
        // Given
        val suvEntities = listOf(
            createVehicleEntity("1", 1, "Kia Sportage", "SUV")
        )
        coEvery { localDataSource.observeVehiclesByCategory("SUV") } returns flowOf(suvEntities)

        // When
        val result = repository.observeVehiclesByCategory(VehicleCategory.SUV).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("SUV", result[0].categoria.name)
    }

    // ==================== searchVehicles ====================

    @Test
    fun `searchVehicles agrega wildcards a la query`() = runTest {
        // Given
        val entities = listOf(
            createVehicleEntity("1", 1, "Kia Sportage", "SUV")
        )
        coEvery { localDataSource.searchVehicles("%sport%") } returns flowOf(entities)

        // When
        val result = repository.searchVehicles("sport").first()

        // Then
        assertEquals(1, result.size)
        coVerify { localDataSource.searchVehicles("%sport%") }
    }

    // ==================== getVehicle ====================

    @Test
    fun `getVehicle retorna vehiculo local cuando existe por id`() = runTest {
        // Given
        val entity = createVehicleEntity("uuid-1", 1, "Kia K5", "Sedan")
        coEvery { localDataSource.getById("uuid-1") } returns entity

        // When
        val result = repository.getVehicle("uuid-1")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Kia K5", (result as Resource.Success).data.modelo)
        coVerify(exactly = 0) { remoteDataSource.getVehiculoById(any()) }
    }

    @Test
    fun `getVehicle busca por remoteId cuando id es numerico`() = runTest {
        // Given
        val entity = createVehicleEntity("uuid-1", 5, "Kia Seltos", "SUV")
        coEvery { localDataSource.getById("5") } returns null
        coEvery { localDataSource.getByRemoteId(5) } returns entity

        // When
        val result = repository.getVehicle("5")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Kia Seltos", (result as Resource.Success).data.modelo)
    }

    @Test
    fun `getVehicle consulta remoto cuando no existe localmente`() = runTest {
        // Given
        val vehiculoDto = createVehiculoDto(10, "Kia EV6", "Luxury")
        coEvery { localDataSource.getById("10") } returns null
        coEvery { localDataSource.getByRemoteId(10) } returns null
        coEvery { remoteDataSource.getVehiculoById(10) } returns Resource.Success(vehiculoDto)

        // When
        val result = repository.getVehicle("10")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Kia EV6", (result as Resource.Success).data.modelo)
    }

    @Test
    fun `getVehicle retorna error cuando id no es valido`() = runTest {
        // Given
        coEvery { localDataSource.getById("invalid-uuid") } returns null

        // When
        val result = repository.getVehicle("invalid-uuid")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("ID inválido", (result as Resource.Error).message)
    }

    @Test
    fun `getVehicle retorna error cuando remoto falla`() = runTest {
        // Given
        coEvery { localDataSource.getById("99") } returns null
        coEvery { localDataSource.getByRemoteId(99) } returns null
        coEvery { remoteDataSource.getVehiculoById(99) } returns Resource.Error("No encontrado")

        // When
        val result = repository.getVehicle("99")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No encontrado", (result as Resource.Error).message)
    }

    // ==================== refreshVehicles ====================

    @Test
    fun `refreshVehicles limpia y actualiza datos locales cuando es exitoso`() = runTest {
        // Given
        val dtos = listOf(
            createVehiculoDto(1, "Kia Sportage", "SUV"),
            createVehiculoDto(2, "Kia Rio", "Compact")
        )
        coEvery { remoteDataSource.getAllVehiculos() } returns Resource.Success(dtos)
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.insertVehicles(any()) } just Runs

        // When
        val result = repository.refreshVehicles()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { localDataSource.deleteAll() }
        coVerify { localDataSource.insertVehicles(any()) }
    }

    @Test
    fun `refreshVehicles retorna error cuando falla remoto`() = runTest {
        // Given
        coEvery { remoteDataSource.getAllVehiculos() } returns Resource.Error("Sin conexión")

        // When
        val result = repository.refreshVehicles()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Sin conexión", (result as Resource.Error).message)
        coVerify(exactly = 0) { localDataSource.deleteAll() }
    }

    // ==================== createVehicle ====================

    @Test
    fun `createVehicle guarda localmente cuando remoto es exitoso`() = runTest {
        // Given
        val params = createVehicleParams(
            modelo = "Kia Niro",
            descripcion = "Híbrido",
            categoria = "SUV",
            precioPorDia = 75.0
        )
        val responseDto = createVehiculoDto(100, "Kia Niro", "SUV")

        coEvery { remoteDataSource.createVehiculo(params) } returns Resource.Success(responseDto)
        coEvery { localDataSource.insertVehicle(any()) } just Runs

        // When
        val result = repository.createVehicle(params)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Kia Niro", (result as Resource.Success).data.modelo)
        coVerify { localDataSource.insertVehicle(any()) }
    }

    @Test
    fun `createVehicle propaga error del remoto`() = runTest {
        // Given
        val params = createVehicleParams(
            modelo = "Test",
            descripcion = "Desc",
            categoria = "Sedan",
            precioPorDia = 50.0
        )
        coEvery { remoteDataSource.createVehiculo(params) } returns Resource.Error("Error al crear")

        // When
        val result = repository.createVehicle(params)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Error al crear", (result as Resource.Error).message)
        coVerify(exactly = 0) { localDataSource.insertVehicle(any()) }
    }

    // ==================== updateVehicle ====================

    @Test
    fun `updateVehicle retorna error cuando id es null`() = runTest {
        // Given
        val params = createVehicleParams(
            id = null,
            modelo = "Kia Update",
            descripcion = "Desc",
            categoria = "SUV",
            precioPorDia = 80.0
        )

        // When
        val result = repository.updateVehicle(params)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("ID requerido para actualizar", (result as Resource.Error).message)
    }

    @Test
    fun `updateVehicle retorna error cuando id no es valido`() = runTest {
        // Given
        val params = createVehicleParams(
            id = "invalid-uuid",
            modelo = "Kia Update",
            descripcion = "Desc",
            categoria = "SUV",
            precioPorDia = 80.0
        )
        coEvery { localDataSource.getById("invalid-uuid") } returns null

        // When
        val result = repository.updateVehicle(params)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("ID inválido", (result as Resource.Error).message)
    }

    @Test
    fun `updateVehicle usa remoteId de entidad local`() = runTest {
        // Given
        val params = createVehicleParams(
            id = "uuid-1",
            modelo = "Kia Updated",
            descripcion = "Desc",
            categoria = "SUV",
            precioPorDia = 90.0
        )
        val entity = createVehicleEntity("uuid-1", 5, "Kia Old", "SUV")

        coEvery { localDataSource.getById("uuid-1") } returns entity
        coEvery { remoteDataSource.updateVehiculo(5, params) } returns Resource.Success(Unit)
        coEvery { remoteDataSource.getAllVehiculos() } returns Resource.Success(emptyList())
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.insertVehicles(any()) } just Runs

        // When
        val result = repository.updateVehicle(params)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.updateVehiculo(5, params) }
    }

    @Test
    fun `updateVehicle usa id numerico como remoteId`() = runTest {
        // Given
        val params = createVehicleParams(
            id = "10",
            modelo = "Kia Updated",
            descripcion = "Desc",
            categoria = "Sedan",
            precioPorDia = 85.0
        )

        coEvery { localDataSource.getById("10") } returns null
        coEvery { remoteDataSource.updateVehiculo(10, params) } returns Resource.Success(Unit)
        coEvery { remoteDataSource.getAllVehiculos() } returns Resource.Success(emptyList())
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.insertVehicles(any()) } just Runs

        // When
        val result = repository.updateVehicle(params)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.updateVehiculo(10, params) }
    }

    @Test
    fun `updateVehicle refresca vehiculos despues de actualizar`() = runTest {
        // Given
        val params = createVehicleParams(
            id = "5",
            modelo = "Updated",
            descripcion = "Desc",
            categoria = "SUV",
            precioPorDia = 100.0
        )
        val refreshedDtos = listOf(createVehiculoDto(5, "Updated", "SUV"))

        coEvery { localDataSource.getById("5") } returns null
        coEvery { remoteDataSource.updateVehiculo(5, params) } returns Resource.Success(Unit)
        coEvery { remoteDataSource.getAllVehiculos() } returns Resource.Success(refreshedDtos)
        coEvery { localDataSource.deleteAll() } just Runs
        coEvery { localDataSource.insertVehicles(any()) } just Runs

        // When
        repository.updateVehicle(params)

        // Then
        coVerify { remoteDataSource.getAllVehiculos() }
        coVerify { localDataSource.deleteAll() }
        coVerify { localDataSource.insertVehicles(any()) }
    }

    @Test
    fun `updateVehicle propaga error del remoto`() = runTest {
        // Given
        val params = createVehicleParams(
            id = "uuid-1",
            modelo = "Test",
            descripcion = "Desc",
            categoria = "SUV",
            precioPorDia = 80.0
        )
        val entity = createVehicleEntity("uuid-1", 5, "Old", "SUV")

        coEvery { localDataSource.getById("uuid-1") } returns entity
        coEvery { remoteDataSource.updateVehiculo(5, params) } returns Resource.Error("Error al actualizar")

        // When
        val result = repository.updateVehicle(params)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Error al actualizar", (result as Resource.Error).message)
    }

    // ==================== deleteVehicle ====================

    @Test
    fun `deleteVehicle elimina usando remoteId de entidad local`() = runTest {
        // Given
        val entity = createVehicleEntity("uuid-1", 5, "ToDelete", "SUV")

        coEvery { localDataSource.getById("uuid-1") } returns entity
        coEvery { remoteDataSource.deleteVehiculo(5) } returns Resource.Success(Unit)
        coEvery { localDataSource.deleteById("uuid-1") } just Runs

        // When
        val result = repository.deleteVehicle("uuid-1")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deleteVehiculo(5) }
        coVerify { localDataSource.deleteById("uuid-1") }
    }

    @Test
    fun `deleteVehicle usa id numerico como remoteId`() = runTest {
        // Given
        coEvery { localDataSource.getById("10") } returns null
        coEvery { remoteDataSource.deleteVehiculo(10) } returns Resource.Success(Unit)
        coEvery { localDataSource.deleteById("10") } just Runs

        // When
        val result = repository.deleteVehicle("10")

        // Then
        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deleteVehiculo(10) }
    }

    @Test
    fun `deleteVehicle retorna error cuando id no es valido`() = runTest {
        // Given
        coEvery { localDataSource.getById("invalid") } returns null

        // When
        val result = repository.deleteVehicle("invalid")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("ID inválido", (result as Resource.Error).message)
    }

    @Test
    fun `deleteVehicle propaga error del remoto`() = runTest {
        // Given
        val entity = createVehicleEntity("uuid-1", 5, "Vehicle", "SUV")

        coEvery { localDataSource.getById("uuid-1") } returns entity
        coEvery { remoteDataSource.deleteVehiculo(5) } returns Resource.Error("No autorizado")

        // When
        val result = repository.deleteVehicle("uuid-1")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No autorizado", (result as Resource.Error).message)
        coVerify(exactly = 0) { localDataSource.deleteById(any()) }
    }

    // ==================== Helper Functions ====================

    private fun createVehicleEntity(
        id: String,
        remoteId: Int,
        modelo: String,
        categoria: String
    ) = VehicleEntity(
        id = id,
        remoteId = remoteId,
        modelo = modelo,
        descripcion = "Descripción de $modelo",
        categoria = categoria,
        asientos = 5,
        transmision = "Automático",
        precioPorDia = 50.0,
        imagenUrl = "https://img.com/$modelo.jpg",
        disponible = true,
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false
    )

    private fun createVehiculoDto(
        id: Int,
        modelo: String,
        categoria: String
    ) = VehiculoDto(
        vehiculoId = id,
        modelo = modelo,
        descripcion = "Descripción de $modelo",
        categoria = categoria,
        asientos = 5,
        transmision = "Automático",
        precioPorDia = 50.0,
        imagenUrl = "https://img.com/$modelo.jpg",
        disponible = true,
        fechaIngreso = null
    )

    private fun createVehicleParams(
        id: String? = null,
        modelo: String,
        descripcion: String,
        categoria: String,
        precioPorDia: Double,
        asientos: Int = 5,
        transmision: String = "Automático",
        imagenUrl: String = "https://img.com/default.jpg"
    ) = VehicleParams(
        id = id,
        modelo = modelo,
        descripcion = descripcion,
        categoria = categoria,
        asientos = asientos,
        transmision = transmision,
        precioPorDia = precioPorDia,
        imagenUrl = imagenUrl
    )
}
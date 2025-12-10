package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.VehicleDtos.VehiculoDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class VehiculoRemoteDataSourceTest {
    private lateinit var dataSource: VehiculoRemoteDataSource
    private lateinit var apiService: ApiService
    @Before
    fun setup() {
        apiService = mockk()
        dataSource = VehiculoRemoteDataSource(apiService)
    }
    @Test
    fun `getAllVehiculos retorna lista cuando API responde 200`() = runTest {
        val vehiculos = listOf(
            VehiculoDto(
                vehiculoId = 1,
                modelo = "Kia K5",
                descripcion = "Sedan",
                categoria = "SUV",
                asientos = 5,
                transmision = "Automatic",
                precioPorDia = 100.0,
                imagenUrl = "http://example.com/k5.jpg",
                disponible = true,
                fechaIngreso = "2025-01-01"
            )
        )
        coEvery { apiService.getAllVehiculos() } returns Response.success(vehiculos)

        val result = dataSource.getAllVehiculos()

        assertNotNull(result)
        assertEquals(1, result?.size)
        assertEquals("Kia K5", result?.get(0)?.modelo)
    }

    @Test
    fun `getAllVehiculos retorna null cuando API falla`() = runTest {
        coEvery { apiService.getAllVehiculos() } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.getAllVehiculos()

        assertNull(result)
    }

    @Test
    fun `getAllVehiculos retorna null cuando hay excepcion`() = runTest {
        coEvery { apiService.getAllVehiculos() } throws IOException("Network error")

        val result = dataSource.getAllVehiculos()

        assertNull(result)
    }
    @Test
    fun `getVehiculoById retorna vehiculo cuando existe`() = runTest {
        val vehiculo = VehiculoDto(
            vehiculoId = 1,
            modelo = "Kia K5",
            descripcion = "Sedan",
            categoria = "SUV",
            asientos = 5,
            transmision = "Automatic",
            precioPorDia = 100.0,
            imagenUrl = "http://example.com/k5.jpg",
            disponible = true,
            fechaIngreso = "2025-01-01"
        )
        coEvery { apiService.getVehiculoById(1) } returns Response.success(vehiculo)

        val result = dataSource.getVehiculoById(1)

        assertNotNull(result)
        assertEquals("Kia K5", result?.modelo)
    }
    @Test
    fun `getVehiculoById retorna null cuando no existe`() = runTest {
        coEvery { apiService.getVehiculoById(999) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.getVehiculoById(999)

        assertNull(result)
    }

    @Test
    fun `deleteVehiculo retorna true cuando API responde 200`() = runTest {
        coEvery { apiService.deleteVehiculo(1) } returns Response.success(Unit)

        val result = dataSource.deleteVehiculo(1)

        assertTrue(result)
    }
    @Test
    fun `deleteVehiculo retorna false cuando API falla`() = runTest {
        coEvery { apiService.deleteVehiculo(1) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.deleteVehiculo(1)

        assertFalse(result)
    }
    @Test
    fun `deleteVehiculo retorna false cuando hay excepcion`() = runTest {
        coEvery { apiService.deleteVehiculo(1) } throws IOException("Network error")

        val result = dataSource.deleteVehiculo(1)

        assertFalse(result)
    }
}
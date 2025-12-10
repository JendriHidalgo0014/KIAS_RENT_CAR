package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.ReservacionDto
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.ReservacionRequest
import edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos.EstadoRequest
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
class ReservacionRemoteDataSourceTest {

    private lateinit var dataSource: ReservacionRemoteDataSource
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = mockk()
        dataSource = ReservacionRemoteDataSource(apiService)
    }

    @Test
    fun `createReservacion retorna ReservacionDto cuando API responde 200`() = runTest {
        val request = ReservacionRequest(
            reservaId = 0,
            vehiculoId = 1,
            usuarioId = "1",
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            lugarRecogida = "Aeropuerto",
            lugarDevolucion = "Aeropuerto",
            estado = "Confirmada",
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            codigoReserva = "KR-123456"
        )
        val response = ReservacionDto(
            reservacionId = 1,
            usuarioId = 1,
            vehiculoId = 1,
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            ubicacionRecogidaId = 1,
            ubicacionDevolucionId = 1,
            estado = "Confirmada",
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            codigoReserva = "KR-123456",
            fechaCreacion = "2025-01-10"
        )
        coEvery { apiService.createReservacion(request) } returns Response.success(response)

        val result = dataSource.createReservacion(request)

        assertNotNull(result)
        assertEquals(1, result?.reservacionId)
    }

    @Test
    fun `createReservacion retorna null cuando API falla`() = runTest {
        val request = ReservacionRequest(
            reservaId = 0,
            vehiculoId = 1,
            usuarioId = "1",
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            lugarRecogida = "Aeropuerto",
            lugarDevolucion = "Aeropuerto",
            estado = "Confirmada",
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            codigoReserva = "KR-123456"
        )
        coEvery { apiService.createReservacion(request) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.createReservacion(request)

        assertNull(result)
    }

    @Test
    fun `createReservacion retorna null cuando hay excepcion`() = runTest {
        val request = ReservacionRequest(
            reservaId = 0,
            vehiculoId = 1,
            usuarioId = "1",
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            lugarRecogida = "Aeropuerto",
            lugarDevolucion = "Aeropuerto",
            estado = "Confirmada",
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            codigoReserva = "KR-123456"
        )
        coEvery { apiService.createReservacion(request) } throws IOException("Network error")

        val result = dataSource.createReservacion(request)

        assertNull(result)
    }

    @Test
    fun `updateEstado retorna true cuando API responde 200`() = runTest {
        val reservacionId = 1
        val estado = "Completada"
        coEvery { apiService.updateEstadoReservacion(reservacionId, EstadoRequest(estado)) } returns Response.success(Unit)

        val result = dataSource.updateEstado(reservacionId, estado)

        assertTrue(result)
    }

    @Test
    fun `updateEstado retorna false cuando API falla`() = runTest {
        val reservacionId = 1
        val estado = "Completada"
        coEvery { apiService.updateEstadoReservacion(reservacionId, EstadoRequest(estado)) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.updateEstado(reservacionId, estado)

        assertFalse(result)
    }

    @Test
    fun `getReservaciones retorna lista cuando API responde 200`() = runTest {
        val reservaciones = listOf(
            ReservacionDto(
                reservacionId = 1,
                usuarioId = 1,
                vehiculoId = 1,
                fechaRecogida = "2025-01-15",
                horaRecogida = "10:00",
                fechaDevolucion = "2025-01-20",
                horaDevolucion = "10:00",
                ubicacionRecogidaId = 1,
                ubicacionDevolucionId = 1,
                estado = "Confirmada",
                subtotal = 500.0,
                impuestos = 90.0,
                total = 590.0,
                codigoReserva = "KR-123456",
                fechaCreacion = "2025-01-10"
            )
        )
        coEvery { apiService.getReservaciones() } returns Response.success(reservaciones)

        val result = dataSource.getReservaciones()

        assertNotNull(result)
        assertEquals(1, result?.size)
    }

    @Test
    fun `getReservaciones retorna null cuando API falla`() = runTest {
        coEvery { apiService.getReservaciones() } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.getReservaciones()

        assertNull(result)
    }
}
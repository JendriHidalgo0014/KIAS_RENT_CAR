package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateReservacionUseCaseTest {

    private lateinit var useCase: CreateReservacionUseCase
    private lateinit var repository: ReservacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = CreateReservacionUseCase(repository)
    }

    @Test
    fun `invoke crea reservacion exitosamente cuando hay config`() = runTest {
        val config = ReservationConfig(
            vehicleId = "1",
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            lugarRecogida = "Aeropuerto",
            lugarDevolucion = "Aeropuerto",
            dias = 5,
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            ubicacionRecogidaId = 1,
            ubicacionDevolucionId = 1
        )
        val reservacion = Reservacion(
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
        coEvery { repository.getReservationConfig() } returns config
        coEvery { repository.createReservacion(config) } returns Resource.Success(reservacion)

        val result = useCase()

        assertTrue(result is Resource.Success)
        assertEquals(reservacion, (result as Resource.Success).data)
        coVerify { repository.getReservationConfig() }
        coVerify { repository.createReservacion(config) }
    }

    @Test
    fun `invoke retorna error cuando no hay config`() = runTest {
        coEvery { repository.getReservationConfig() } returns null

        val result = useCase()

        assertTrue(result is Resource.Error)
        assertEquals("No hay configuración de reserva", (result as Resource.Error).message)
    }

    @Test
    fun `invoke propaga errores del repositorio`() = runTest {
        val config = ReservationConfig(
            vehicleId = "1",
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            lugarRecogida = "Aeropuerto",
            lugarDevolucion = "Aeropuerto",
            dias = 5,
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            ubicacionRecogidaId = 1,
            ubicacionDevolucionId = 1
        )
        val errorMessage = "Error de conexión"
        coEvery { repository.getReservationConfig() } returns config
        coEvery { repository.createReservacion(config) } returns Resource.Error(errorMessage)

        val result = useCase()

        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}
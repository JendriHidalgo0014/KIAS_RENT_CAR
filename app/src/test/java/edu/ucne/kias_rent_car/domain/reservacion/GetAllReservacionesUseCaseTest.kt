package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.domain.model.Reservacion
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
class GetAllReservacionesUseCaseTest {

    private lateinit var useCase: GetAllReservacionesUseCase
    private lateinit var repository: ReservacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAllReservacionesUseCase(repository)
    }

    @Test
    fun `invoke retorna lista de reservaciones`() = runTest {
        val reservaciones = listOf(
            Reservacion(
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
        coEvery { repository.getReservaciones() } returns reservaciones

        val result = useCase()

        assertEquals(1, result.size)
        assertEquals("KR-123456", result[0].codigoReserva)
        coVerify { repository.getReservaciones() }
    }

    @Test
    fun `invoke retorna lista vacia cuando no hay reservaciones`() = runTest {
        coEvery { repository.getReservaciones() } returns emptyList()

        val result = useCase()

        assertTrue(result.isEmpty())
    }
}
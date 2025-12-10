package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetAllReservacionesUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AdminReservasViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: AdminReservasViewModel
    private lateinit var getAllReservacionesUseCase: GetAllReservacionesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllReservacionesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga reservaciones automaticamente`() = runTest {
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
        coEvery { getAllReservacionesUseCase() } returns reservaciones

        viewModel = AdminReservasViewModel(getAllReservacionesUseCase)
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.reservaciones.size)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `onFiltroChanged filtra por Reservado`() = runTest {
        val reservaciones = listOf(
            Reservacion(
                reservacionId = 1, usuarioId = 1, vehiculoId = 1,
                fechaRecogida = "2025-01-15", horaRecogida = "10:00",
                fechaDevolucion = "2025-01-20", horaDevolucion = "10:00",
                ubicacionRecogidaId = 1, ubicacionDevolucionId = 1,
                estado = "Confirmada", subtotal = 500.0, impuestos = 90.0,
                total = 590.0, codigoReserva = "KR-1", fechaCreacion = "2025-01-10"
            ),
            Reservacion(
                reservacionId = 2, usuarioId = 1, vehiculoId = 2,
                fechaRecogida = "2025-01-15", horaRecogida = "10:00",
                fechaDevolucion = "2025-01-20", horaDevolucion = "10:00",
                ubicacionRecogidaId = 1, ubicacionDevolucionId = 1,
                estado = "Finalizada", subtotal = 500.0, impuestos = 90.0,
                total = 590.0, codigoReserva = "KR-2", fechaCreacion = "2025-01-10"
            )
        )
        coEvery { getAllReservacionesUseCase() } returns reservaciones

        viewModel = AdminReservasViewModel(getAllReservacionesUseCase)
        advanceUntilIdle()

        viewModel.onFiltroChanged("Reservado")

        assertEquals("Reservado", viewModel.state.value.filtroActual)
        assertEquals(1, viewModel.state.value.reservaciones.size)
    }

    @Test
    fun `onFiltroChanged muestra todos con Todos`() = runTest {
        val reservaciones = listOf(
            Reservacion(
                reservacionId = 1, usuarioId = 1, vehiculoId = 1,
                fechaRecogida = "2025-01-15", horaRecogida = "10:00",
                fechaDevolucion = "2025-01-20", horaDevolucion = "10:00",
                ubicacionRecogidaId = 1, ubicacionDevolucionId = 1,
                estado = "Confirmada", subtotal = 500.0, impuestos = 90.0,
                total = 590.0, codigoReserva = "KR-1", fechaCreacion = "2025-01-10"
            ),
            Reservacion(
                reservacionId = 2, usuarioId = 1, vehiculoId = 2,
                fechaRecogida = "2025-01-15", horaRecogida = "10:00",
                fechaDevolucion = "2025-01-20", horaDevolucion = "10:00",
                ubicacionRecogidaId = 1, ubicacionDevolucionId = 1,
                estado = "Finalizada", subtotal = 500.0, impuestos = 90.0,
                total = 590.0, codigoReserva = "KR-2", fechaCreacion = "2025-01-10"
            )
        )
        coEvery { getAllReservacionesUseCase() } returns reservaciones

        viewModel = AdminReservasViewModel(getAllReservacionesUseCase)
        advanceUntilIdle()

        viewModel.onFiltroChanged("Todos")

        assertEquals(2, viewModel.state.value.reservaciones.size)
    }
}
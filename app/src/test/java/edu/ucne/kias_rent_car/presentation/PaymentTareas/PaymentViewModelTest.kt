package edu.ucne.kias_rent_car.presentation.PaymentTareas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.CreateReservacionUseCase
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservationConfigUseCase
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
class PaymentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: PaymentViewModel
    private lateinit var createReservacionUseCase: CreateReservacionUseCase
    private lateinit var getReservationConfigUseCase: GetReservationConfigUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        createReservacionUseCase = mockk()
        getReservationConfigUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga configuracion de reserva`() = runTest {
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
        coEvery { getReservationConfigUseCase() } returns config

        viewModel = PaymentViewModel(createReservacionUseCase, getReservationConfigUseCase)
        viewModel.init("1")
        advanceUntilIdle()

        assertEquals("1", viewModel.state.value.vehicleId)
        assertEquals(590.0, viewModel.state.value.total, 0.01)
    }

    @Test
    fun `onEvent MetodoPagoChanged actualiza metodo`() = runTest {
        coEvery { getReservationConfigUseCase() } returns null
        viewModel = PaymentViewModel(createReservacionUseCase, getReservationConfigUseCase)

        viewModel.onEvent(PaymentEvent.MetodoPagoChanged(MetodoPago.BILLETERA))

        assertEquals(MetodoPago.BILLETERA, viewModel.state.value.metodoPago)
    }

    @Test
    fun `onEvent NumeroTarjetaChanged limita a 16 digitos`() = runTest {
        coEvery { getReservationConfigUseCase() } returns null
        viewModel = PaymentViewModel(createReservacionUseCase, getReservationConfigUseCase)

        viewModel.onEvent(PaymentEvent.NumeroTarjetaChanged("12345678901234567890"))

        assertEquals("1234567890123456", viewModel.state.value.numeroTarjeta)
    }

    @Test
    fun `onEvent CvvChanged limita a 4 digitos`() = runTest {
        coEvery { getReservationConfigUseCase() } returns null
        viewModel = PaymentViewModel(createReservacionUseCase, getReservationConfigUseCase)

        viewModel.onEvent(PaymentEvent.CvvChanged("12345"))

        assertEquals("1234", viewModel.state.value.cvv)
    }

    @Test
    fun `procesarPago exitoso actualiza estado`() = runTest {
        val reservacion = Reservacion(
            reservacionId = 123,
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
        coEvery { getReservationConfigUseCase() } returns null
        coEvery { createReservacionUseCase() } returns Resource.Success(reservacion)

        viewModel = PaymentViewModel(createReservacionUseCase, getReservationConfigUseCase)
        viewModel.procesarPago()
        advanceUntilIdle()

        assertTrue(viewModel.state.value.paymentSuccess)
        assertEquals("123", viewModel.state.value.reservacionId)
    }

    @Test
    fun `procesarPago con error actualiza estado`() = runTest {
        coEvery { getReservationConfigUseCase() } returns null
        coEvery { createReservacionUseCase() } returns Resource.Error("Error de conexión")

        viewModel = PaymentViewModel(createReservacionUseCase, getReservationConfigUseCase)
        viewModel.procesarPago()
        advanceUntilIdle()

        assertFalse(viewModel.state.value.paymentSuccess)
        assertEquals("Error de conexión", viewModel.state.value.error)
    }
}
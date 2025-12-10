package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.usecase.Mensaje.GetAllMensajesUseCase
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
class AdminMensajesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: AdminMensajesViewModel
    private lateinit var getAllMensajesUseCase: GetAllMensajesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllMensajesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga mensajes automaticamente`() = runTest {
        val mensajes = listOf(
            Mensaje(
                mensajeId = 1,
                usuarioId = 1,
                nombreUsuario = "Juan",
                asunto = "Consulta",
                contenido = "Pregunta",
                respuesta = null,
                fechaCreacion = "2025-01-10",
                leido = false
            )
        )
        coEvery { getAllMensajesUseCase() } returns mensajes

        viewModel = AdminMensajesViewModel(getAllMensajesUseCase)
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.mensajes.size)
        assertEquals("Juan", viewModel.state.value.mensajes[0].nombreUsuario)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `init maneja lista vacia`() = runTest {
        coEvery { getAllMensajesUseCase() } returns emptyList()

        viewModel = AdminMensajesViewModel(getAllMensajesUseCase)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.mensajes.isEmpty())
        assertFalse(viewModel.state.value.isLoading)
    }
}
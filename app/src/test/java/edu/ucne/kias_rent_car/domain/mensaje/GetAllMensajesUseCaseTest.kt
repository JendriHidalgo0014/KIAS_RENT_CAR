package edu.ucne.kias_rent_car.domain.usecase.Mensaje

import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllMensajesUseCaseTest {

    private lateinit var useCase: GetAllMensajesUseCase
    private lateinit var repository: MensajeRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAllMensajesUseCase(repository)
    }

    @Test
    fun `invoke retorna lista de mensajes`() = runTest {
        val mensajes = listOf(
            Mensaje(
                mensajeId = 1,
                usuarioId = 1,
                nombreUsuario = "Juan",
                asunto = "Consulta",
                contenido = "Tengo una pregunta",
                respuesta = null,
                fechaCreacion = "2025-01-10",
                leido = false
            )
        )
        coEvery { repository.getMensajes() } returns mensajes

        val result = useCase()

        assertEquals(1, result.size)
        assertEquals("Juan", result[0].nombreUsuario)
        coVerify { repository.getMensajes() }
    }

    @Test
    fun `invoke retorna lista vacia cuando no hay mensajes`() = runTest {
        coEvery { repository.getMensajes() } returns emptyList()

        val result = useCase()

        assertTrue(result.isEmpty())
    }
}
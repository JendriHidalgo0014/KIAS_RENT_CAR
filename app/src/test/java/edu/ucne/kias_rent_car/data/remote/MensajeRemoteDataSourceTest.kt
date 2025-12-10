package edu.ucne.kias_rent_car.data.remote.datasource

import edu.ucne.kias_rent_car.data.remote.ApiService
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.MensajeDto
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.MensajeRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos.RespuestaRequest
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
class MensajeRemoteDataSourceTest {

    private lateinit var dataSource: MensajeRemoteDataSource
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = mockk()
        dataSource = MensajeRemoteDataSource(apiService)
    }

    @Test
    fun `sendMensaje retorna MensajeDto cuando API responde 200`() = runTest {
        val usuarioId = 1
        val asunto = "Consulta"
        val contenido = "Tengo una pregunta"
        val request = MensajeRequest(usuarioId = usuarioId, asunto = asunto, contenido = contenido)
        val response = MensajeDto(
            mensajeId = 1,
            usuarioId = usuarioId,
            nombreUsuario = "Juan",
            asunto = asunto,
            contenido = contenido,
            respuesta = null,
            fechaCreacion = "2025-01-10",
            leido = false
        )
        coEvery { apiService.createMensaje(request) } returns Response.success(response)

        val result = dataSource.sendMensaje(usuarioId, asunto, contenido)

        assertNotNull(result)
        assertEquals(1, result?.mensajeId)
    }

    @Test
    fun `sendMensaje retorna null cuando API falla`() = runTest {
        coEvery { apiService.createMensaje(any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.sendMensaje(1, "Asunto", "Contenido")

        assertNull(result)
    }

    @Test
    fun `sendMensaje retorna null cuando hay excepcion`() = runTest {
        coEvery { apiService.createMensaje(any()) } throws IOException("Network error")

        val result = dataSource.sendMensaje(1, "Asunto", "Contenido")

        assertNull(result)
    }

    @Test
    fun `responderMensaje retorna true cuando API responde 200`() = runTest {
        val mensajeId = 1
        val respuesta = "Gracias por su consulta"
        coEvery { apiService.responderMensaje(mensajeId, RespuestaRequest(respuesta)) } returns Response.success(Unit)

        val result = dataSource.responderMensaje(mensajeId, respuesta)

        assertTrue(result)
    }

    @Test
    fun `responderMensaje retorna false cuando API falla`() = runTest {
        val mensajeId = 1
        val respuesta = "Gracias"
        coEvery { apiService.responderMensaje(mensajeId, RespuestaRequest(respuesta)) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.responderMensaje(mensajeId, respuesta)

        assertFalse(result)
    }

    @Test
    fun `getMensajes retorna lista cuando API responde 200`() = runTest {
        val mensajes = listOf(
            MensajeDto(
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
        coEvery { apiService.getMensajes() } returns Response.success(mensajes)

        val result = dataSource.getMensajes()

        assertNotNull(result)
        assertEquals(1, result?.size)
    }

    @Test
    fun `getMensajes retorna null cuando API falla`() = runTest {
        coEvery { apiService.getMensajes() } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        val result = dataSource.getMensajes()

        assertNull(result)
    }
}
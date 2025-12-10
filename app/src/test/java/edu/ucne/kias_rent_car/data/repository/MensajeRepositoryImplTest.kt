package edu.ucne.kias_rent_car.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.MensajeDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.entity.MensajeEntity
import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import edu.ucne.kias_rent_car.data.remote.datasource.MensajeRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MensajeRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: MensajeRepositoryImpl
    private lateinit var context: Context
    private lateinit var remoteDataSource: MensajeRemoteDataSource
    private lateinit var mensajeDao: MensajeDao
    private lateinit var usuarioDao: UsuarioDao

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        mensajeDao = mockk(relaxed = true)
        usuarioDao = mockk(relaxed = true)

        repository = MensajeRepositoryImpl(
            context,
            remoteDataSource,
            mensajeDao,
            usuarioDao
        )
    }

    @Test
    fun `sendMensaje guarda localmente con isPendingCreate true`() = runTest {
        val asunto = "Consulta"
        val contenido = "Tengo una pregunta"
        val usuarioEntity = UsuarioEntity(
            usuarioId = 1,
            nombre = "Juan",
            email = "juan@test.com",
            password = "password123",
            telefono = null,
            rol = "Cliente",
            fechaRegistro = "2025-01-01",
            isLoggedIn = true
        )
        val mensajeSlot = slot<MensajeEntity>()

        coEvery { usuarioDao.getLoggedInUsuario() } returns usuarioEntity
        coEvery { mensajeDao.insertMensaje(capture(mensajeSlot)) } just Runs
        coEvery { remoteDataSource.sendMensaje(any(), any(), any()) } returns null

        repository.sendMensaje(asunto, contenido)

        coVerify { mensajeDao.insertMensaje(any()) }
        assertTrue(mensajeSlot.captured.isPendingCreate)
        assertEquals(asunto, mensajeSlot.captured.asunto)
    }

    @Test
    fun `getMensajes retorna datos locales cuando remoto falla`() = runTest {
        val localEntities = listOf(
            MensajeEntity(
                mensajeId = 1,
                remoteId = 1,
                usuarioId = 1,
                nombreUsuario = "Juan",
                asunto = "Test",
                contenido = "Contenido",
                respuesta = null,
                fechaCreacion = "2025-01-10",
                leido = false,
                isPendingCreate = false,
                isPendingRespuesta = false
            )
        )
        coEvery { remoteDataSource.getMensajes() } throws Exception("Network error")
        coEvery { mensajeDao.getMensajes() } returns localEntities

        val result = repository.getMensajes()

        assertEquals(1, result.size)
        assertEquals("Test", result[0].asunto)
    }

    @Test
    fun `responderMensaje actualiza localmente`() = runTest {
        val mensajeId = 1
        val respuesta = "Gracias por su consulta"
        coEvery { mensajeDao.updateRespuestaLocal(mensajeId, respuesta) } just Runs
        coEvery { remoteDataSource.responderMensaje(mensajeId, respuesta) } returns false

        repository.responderMensaje(mensajeId, respuesta)

        coVerify { mensajeDao.updateRespuestaLocal(mensajeId, respuesta) }
    }
}
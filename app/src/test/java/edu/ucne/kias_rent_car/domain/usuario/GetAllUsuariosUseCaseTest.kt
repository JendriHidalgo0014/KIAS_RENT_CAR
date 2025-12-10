package edu.ucne.kias_rent_car.domain.usecase.Usuario

import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllUsuariosUseCaseTest {

    private lateinit var useCase: GetAllUsuariosUseCase
    private lateinit var repository: UsuarioRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAllUsuariosUseCase(repository)
    }

    @Test
    fun `invoke retorna lista de usuarios`() = runTest {
        val usuarios = listOf(
            Usuario(id = 1, nombre = "Juan", email = "juan@test.com", telefono = null, rol = "Cliente"),
            Usuario(id = 2, nombre = "Maria", email = "maria@test.com", telefono = "1234567890", rol = "Cliente")
        )
        coEvery { repository.getAllUsuarios() } returns usuarios

        val result = useCase()

        assertEquals(2, result.size)
        assertEquals("Juan", result[0].nombre)
        coVerify { repository.getAllUsuarios() }
    }

    @Test
    fun `invoke retorna lista vacia cuando no hay usuarios`() = runTest {
        coEvery { repository.getAllUsuarios() } returns emptyList()

        val result = useCase()

        assertTrue(result.isEmpty())
    }
}
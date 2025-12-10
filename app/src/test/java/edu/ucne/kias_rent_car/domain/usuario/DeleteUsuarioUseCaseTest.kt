package edu.ucne.kias_rent_car.domain.usecase.Usuario

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
class DeleteUsuarioUseCaseTest {

    private lateinit var useCase: DeleteUsuarioUseCase
    private lateinit var repository: UsuarioRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteUsuarioUseCase(repository)
    }

    @Test
    fun `invoke elimina usuario exitosamente`() = runTest {
        val usuarioId = 1
        coEvery { repository.deleteUsuario(usuarioId) } returns true

        val result = useCase(usuarioId)

        assertTrue(result)
        coVerify { repository.deleteUsuario(usuarioId) }
    }

    @Test
    fun `invoke retorna false cuando falla`() = runTest {
        val usuarioId = 999
        coEvery { repository.deleteUsuario(usuarioId) } returns false

        val result = useCase(usuarioId)

        assertFalse(result)
    }
}
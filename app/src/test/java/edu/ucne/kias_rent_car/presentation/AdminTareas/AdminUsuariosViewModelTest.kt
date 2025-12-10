package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.usecase.Usuario.DeleteUsuarioUseCase
import edu.ucne.kias_rent_car.domain.usecase.Usuario.GetAllUsuariosUseCase
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
class AdminUsuariosViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: AdminUsuariosViewModel
    private lateinit var getAllUsuariosUseCase: GetAllUsuariosUseCase
    private lateinit var deleteUsuarioUseCase: DeleteUsuarioUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllUsuariosUseCase = mockk()
        deleteUsuarioUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga usuarios automaticamente`() = runTest {
        val usuarios = listOf(
            Usuario(id = 1, nombre = "Juan", email = "juan@test.com", telefono = null, rol = "Cliente"),
            Usuario(id = 2, nombre = "Maria", email = "maria@test.com", telefono = null, rol = "Cliente")
        )
        coEvery { getAllUsuariosUseCase() } returns usuarios

        viewModel = AdminUsuariosViewModel(getAllUsuariosUseCase, deleteUsuarioUseCase)
        advanceUntilIdle()

        assertEquals(2, viewModel.state.value.usuarios.size)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `onSearchChanged filtra por nombre`() = runTest {
        val usuarios = listOf(
            Usuario(id = 1, nombre = "Juan Perez", email = "juan@test.com", telefono = null, rol = "Cliente"),
            Usuario(id = 2, nombre = "Maria Lopez", email = "maria@test.com", telefono = null, rol = "Cliente")
        )
        coEvery { getAllUsuariosUseCase() } returns usuarios

        viewModel = AdminUsuariosViewModel(getAllUsuariosUseCase, deleteUsuarioUseCase)
        advanceUntilIdle()

        viewModel.onSearchChanged("Juan")

        assertEquals(1, viewModel.state.value.usuarios.size)
        assertEquals("Juan Perez", viewModel.state.value.usuarios[0].nombre)
    }

    @Test
    fun `onSearchChanged filtra por email`() = runTest {
        val usuarios = listOf(
            Usuario(id = 1, nombre = "Juan", email = "juan@test.com", telefono = null, rol = "Cliente"),
            Usuario(id = 2, nombre = "Maria", email = "maria@gmail.com", telefono = null, rol = "Cliente")
        )
        coEvery { getAllUsuariosUseCase() } returns usuarios

        viewModel = AdminUsuariosViewModel(getAllUsuariosUseCase, deleteUsuarioUseCase)
        advanceUntilIdle()

        viewModel.onSearchChanged("gmail")

        assertEquals(1, viewModel.state.value.usuarios.size)
    }

    @Test
    fun `onSearchChanged vacio muestra todos`() = runTest {
        val usuarios = listOf(
            Usuario(id = 1, nombre = "Juan", email = "juan@test.com", telefono = null, rol = "Cliente"),
            Usuario(id = 2, nombre = "Maria", email = "maria@test.com", telefono = null, rol = "Cliente")
        )
        coEvery { getAllUsuariosUseCase() } returns usuarios

        viewModel = AdminUsuariosViewModel(getAllUsuariosUseCase, deleteUsuarioUseCase)
        advanceUntilIdle()

        viewModel.onSearchChanged("Juan")
        assertEquals(1, viewModel.state.value.usuarios.size)

        viewModel.onSearchChanged("")
        assertEquals(2, viewModel.state.value.usuarios.size)
    }

    @Test
    fun `deleteUsuario llama useCase y recarga`() = runTest {
        val usuarios = listOf(
            Usuario(id = 1, nombre = "Juan", email = "juan@test.com", telefono = null, rol = "Cliente")
        )
        coEvery { getAllUsuariosUseCase() } returns usuarios
        coEvery { deleteUsuarioUseCase(1) } returns true

        viewModel = AdminUsuariosViewModel(getAllUsuariosUseCase, deleteUsuarioUseCase)
        advanceUntilIdle()

        viewModel.deleteUsuario(1)
        advanceUntilIdle()

        coVerify { deleteUsuarioUseCase(1) }
        coVerify(atLeast = 2) { getAllUsuariosUseCase() }
    }
}
package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.model.TransmisionType
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetVehiclesByCategoryUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.RefreshVehiclesUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.SearchVehiclesUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HomeViewModel
    private lateinit var getVehiclesByCategoryUseCase: GetVehiclesByCategoryUseCase
    private lateinit var searchVehiclesUseCase: SearchVehiclesUseCase
    private lateinit var refreshVehiclesUseCase: RefreshVehiclesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getVehiclesByCategoryUseCase = mockk()
        searchVehiclesUseCase = mockk()
        refreshVehiclesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createVehicle() = Vehicle(
        id = "1",
        remoteId = 1,
        modelo = "Kia K5",
        descripcion = "Sedan",
        categoria = VehicleCategory.SUV,
        asientos = 5,
        transmision = TransmisionType.AUTOMATIC,
        precioPorDia = 100.0,
        imagenUrl = "",
        disponible = true
    )

    private fun setupDefaultMocks() {
        coEvery { getVehiclesByCategoryUseCase.invoke(any()) } returns flowOf(emptyList())
        coEvery { searchVehiclesUseCase.invoke(any()) } returns flowOf(emptyList())
        coEvery { refreshVehiclesUseCase.invoke() } returns Resource.Success(Unit)
    }

    @Test
    fun `init carga vehiculos y hace refresh`() = runTest {
        val vehicles = listOf(createVehicle())
        coEvery { getVehiclesByCategoryUseCase.invoke(any()) } returns flowOf(vehicles)
        coEvery { refreshVehiclesUseCase.invoke() } returns Resource.Success(Unit)

        viewModel = HomeViewModel(getVehiclesByCategoryUseCase, searchVehiclesUseCase, refreshVehiclesUseCase)
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.vehicles.size)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `onEvent OnCategorySelected cambia categoria`() = runTest {
        setupDefaultMocks()

        viewModel = HomeViewModel(getVehiclesByCategoryUseCase, searchVehiclesUseCase, refreshVehiclesUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeEvent.OnCategorySelected(VehicleCategory.SUV))
        advanceUntilIdle()

        assertEquals(VehicleCategory.SUV, viewModel.state.value.selectedCategory)
    }

    @Test
    fun `onEvent OnSearchQueryChanged actualiza query`() = runTest {
        setupDefaultMocks()

        viewModel = HomeViewModel(getVehiclesByCategoryUseCase, searchVehiclesUseCase, refreshVehiclesUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeEvent.OnSearchQueryChanged("Kia"))
        advanceUntilIdle()

        assertEquals("Kia", viewModel.state.value.searchQuery)
    }

    @Test
    fun `onEvent OnRefresh llama refreshVehiclesUseCase`() = runTest {
        setupDefaultMocks()

        viewModel = HomeViewModel(getVehiclesByCategoryUseCase, searchVehiclesUseCase, refreshVehiclesUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeEvent.OnRefresh)
        advanceUntilIdle()

        coVerify(atLeast = 2) { refreshVehiclesUseCase.invoke() }
    }

    @Test
    fun `onEvent OnErrorDismissed limpia error`() = runTest {
        coEvery { getVehiclesByCategoryUseCase.invoke(any()) } returns flowOf(emptyList())
        coEvery { refreshVehiclesUseCase.invoke() } returns Resource.Error("Error")

        viewModel = HomeViewModel(getVehiclesByCategoryUseCase, searchVehiclesUseCase, refreshVehiclesUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeEvent.OnErrorDismissed)

        assertNull(viewModel.state.value.error)
    }
}
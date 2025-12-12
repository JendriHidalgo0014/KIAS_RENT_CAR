package edu.ucne.kias_rent_car.presentation.HomeClienteTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetVehiclesByCategoryUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.RefreshVehiclesUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.SearchVehiclesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getVehiclesByCategory: GetVehiclesByCategoryUseCase,
    private val searchVehiclesUseCase: SearchVehiclesUseCase,
    private val refreshVehiclesUseCase: RefreshVehiclesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadVehicles()
        refreshFromServer()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = event.category, searchQuery = "") }
                loadVehicles()
            }
            is HomeUiEvent.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                if (event.query.isNotBlank()) {
                    performSearch(event.query)
                } else {
                    loadVehicles()
                }
            }
            HomeUiEvent.OnRefresh -> refreshFromServer()
            HomeUiEvent.OnErrorDismissed -> _state.update { it.copy(error = null) }
            else -> Unit
        }
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getVehiclesByCategory(_state.value.selectedCategory)
                .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                .collect { vehicles -> _state.update { it.copy(vehicles = vehicles, isLoading = false) } }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            searchVehiclesUseCase(query)
                .catch { e -> _state.update { it.copy(error = e.message) } }
                .collect { vehicles -> _state.update { it.copy(vehicles = vehicles) } }
        }
    }

    private fun refreshFromServer() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            when (val result = refreshVehiclesUseCase()) {
                is Resource.Success<*> -> {
                    _state.update { it.copy(isRefreshing = false) }
                    loadVehicles()
                }
                is Resource.Error<*> -> _state.update { it.copy(isRefreshing = false, error = result.message) }
                is Resource.Loading<*> -> {}
            }
        }
    }
}
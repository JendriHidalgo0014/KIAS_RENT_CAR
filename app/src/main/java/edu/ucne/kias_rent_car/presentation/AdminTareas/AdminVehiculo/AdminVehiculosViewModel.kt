package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.DeleteVehicleUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetAllVehiclesUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.SearchVehiclesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminVehiculosViewModel @Inject constructor(
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase,
    private val searchVehiclesUseCase: SearchVehiclesUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AdminVehiculosUiState())
    val state: StateFlow<AdminVehiculosUiState> = _state.asStateFlow()

    init {
        loadVehiculos()
    }
    private fun loadVehiculos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getAllVehiclesUseCase().collect { vehiculos ->
                _state.update {
                    it.copy(
                        vehiculos = vehiculos,
                        isLoading = false
                    )
                }
            }
        }
    }
    fun onSearchChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }

        viewModelScope.launch {
            if (query.isBlank()) {
                loadVehiculos()
            } else {
                searchVehiclesUseCase(query).collect { vehiculos ->
                    _state.update { it.copy(vehiculos = vehiculos) }
                }
            }
        }
    }
    fun deleteVehiculo(id: String) {
        viewModelScope.launch {
            deleteVehicleUseCase(id)
            loadVehiculos()
        }
    }
}
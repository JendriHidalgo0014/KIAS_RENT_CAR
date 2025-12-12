package edu.ucne.kias_rent_car.presentation.ClienteTareas.VehiculoDetalleTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetVehicleDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleDetailViewModel @Inject constructor(
    private val getVehicleDetailUseCase: GetVehicleDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VehicleDetailUiState())
    val state: StateFlow<VehicleDetailUiState> = _state.asStateFlow()

    fun onEvent(event: VehicleDetailUiEvent) {
        when (event) {
            is VehicleDetailUiEvent.LoadVehicle -> loadVehicle(event.vehicleId)
            else -> Unit
        }
    }

    private fun loadVehicle(vehicleId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val vehicle = getVehicleDetailUseCase(vehicleId)
                if (vehicle != null) {
                    _state.update { it.copy(vehicle = vehicle, isLoading = false) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Vehículo no encontrado") }
                }
            } catch (_: Exception) {
                _state.update { it.copy(isLoading = false, error = "Error al cargar el vehículo") }
            }
        }
    }
}
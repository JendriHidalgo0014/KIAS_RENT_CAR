package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.VehicleParams
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.CreateVehicleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddVehiculoViewModel @Inject constructor(
    private val createVehicleUseCase: CreateVehicleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddVehiculoUiState())
    val state: StateFlow<AddVehiculoUiState> = _state.asStateFlow()

    fun onEvent(event: AddVehiculoUiEvent) {
        when (event) {
            is AddVehiculoUiEvent.OnModeloChange -> {
                _state.update { it.copy(modelo = event.modelo) }
                validateForm()
            }
            is AddVehiculoUiEvent.OnDescripcionChange -> {
                _state.update { it.copy(descripcion = event.descripcion) }
                validateForm()
            }
            is AddVehiculoUiEvent.OnCategoriaChange -> _state.update { it.copy(categoria = event.categoria) }
            is AddVehiculoUiEvent.OnTransmisionChange -> _state.update { it.copy(transmision = event.transmision) }
            is AddVehiculoUiEvent.OnAsientosChange -> _state.update { it.copy(asientos = event.asientos) }
            is AddVehiculoUiEvent.OnPrecioChange -> {
                _state.update { it.copy(precioPorDia = event.precio) }
                validateForm()
            }
            is AddVehiculoUiEvent.OnFechaIngresoChange -> _state.update { it.copy(fechaIngreso = event.fecha) }
            is AddVehiculoUiEvent.OnImagenUrlChange -> _state.update { it.copy(imagenUrl = event.url) }
            AddVehiculoUiEvent.GuardarVehiculo -> guardarVehiculo()
            else -> Unit
        }
    }

    private fun validateForm() {
        val s = _state.value
        val isValid = s.modelo.isNotBlank() &&
                s.descripcion.isNotBlank() &&
                s.precioPorDia.isNotBlank() &&
                (s.precioPorDia.toDoubleOrNull() ?: 0.0) > 0
        _state.update { it.copy(isFormValid = isValid) }
    }

    private fun guardarVehiculo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val params = VehicleParams(
                modelo = _state.value.modelo,
                descripcion = _state.value.descripcion,
                categoria = _state.value.categoria,
                asientos = _state.value.asientos,
                transmision = _state.value.transmision,
                precioPorDia = _state.value.precioPorDia.toDoubleOrNull() ?: 0.0,
                imagenUrl = _state.value.imagenUrl
            )

            when (val result = createVehicleUseCase(params)) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, saveSuccess = true) }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> Unit
            }
        }
    }
}
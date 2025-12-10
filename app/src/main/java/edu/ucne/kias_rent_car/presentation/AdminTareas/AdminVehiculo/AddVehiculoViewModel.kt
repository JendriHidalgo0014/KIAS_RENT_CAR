package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
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

    fun onModeloChanged(value: String) {
        _state.update { it.copy(modelo = value) }
    }

    fun onDescripcionChanged(value: String) {
        _state.update { it.copy(descripcion = value) }
    }

    fun onFechaIngresoChanged(value: String) {
        _state.update { it.copy(fechaIngreso = value) }
    }

    fun onPrecioChanged(value: String) {
        _state.update { it.copy(precioPorDia = value) }
    }

    fun onImagenUrlChanged(value: String) {
        _state.update { it.copy(imagenUrl = value) }
    }

    fun onCategoriaChanged(value: String) {
        _state.update { it.copy(categoria = value) }
    }

    fun onAsientosChanged(value: Int) {
        _state.update { it.copy(asientos = value) }
    }

    fun onTransmisionChanged(value: String) {
        _state.update { it.copy(transmision = value) }
    }

    fun guardarVehiculo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = createVehicleUseCase(
                modelo = _state.value.modelo,
                descripcion = _state.value.descripcion,
                categoria = _state.value.categoria,
                asientos = _state.value.asientos,
                transmision = _state.value.transmision,
                precioPorDia = _state.value.precioPorDia.toDoubleOrNull() ?: 0.0,
                imagenUrl = _state.value.imagenUrl
            )

            when (result) {
                is Resource.Success<*> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            saveSuccess = true
                        )
                    }
                }
                is Resource.Error<*> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading<*> -> {}
            }
        }
    }
}
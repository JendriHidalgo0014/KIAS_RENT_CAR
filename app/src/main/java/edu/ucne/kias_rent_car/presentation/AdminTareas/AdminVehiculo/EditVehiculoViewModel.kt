package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.DeleteVehicleUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetVehicleDetailUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.UpdateVehicleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditVehiculoViewModel @Inject constructor(
    private val getVehicleDetailUseCase: GetVehicleDetailUseCase,
    private val updateVehicleUseCase: UpdateVehicleUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditVehiculoUiState())
    val state: StateFlow<EditVehiculoUiState> = _state.asStateFlow()

    fun loadVehiculo(vehiculoId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val vehiculo = getVehicleDetailUseCase(vehiculoId)

            vehiculo?.let { v ->
                _state.update {
                    it.copy(
                        vehiculoId = v.id,
                        modelo = v.modelo,
                        descripcion = v.descripcion,
                        precioPorDia = v.precioPorDia.toString(),
                        imagenUrl = v.imagenUrl,
                        categoria = v.categoria.name,
                        asientos = v.asientos,
                        transmision = v.transmision.name,
                        isLoading = false
                    )
                }
            }
        }
    }

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

    fun guardarCambios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = updateVehicleUseCase(
                id = _state.value.vehiculoId,
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

    fun eliminarVehiculo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = deleteVehicleUseCase(_state.value.vehiculoId)

            when (result) {
                is Resource.Success<*> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            deleteSuccess = true
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
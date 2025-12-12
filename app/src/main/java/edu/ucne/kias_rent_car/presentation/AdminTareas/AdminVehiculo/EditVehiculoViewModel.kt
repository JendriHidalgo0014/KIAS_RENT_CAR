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

    fun onEvent(event: EditVehiculoUiEvent) {
        when (event) {
            is EditVehiculoUiEvent.LoadVehiculo -> loadVehiculo(event.id)
            is EditVehiculoUiEvent.OnModeloChange -> { _state.update { it.copy(modelo = event.modelo) }; validateForm() }
            is EditVehiculoUiEvent.OnDescripcionChange -> { _state.update { it.copy(descripcion = event.descripcion) }; validateForm() }
            is EditVehiculoUiEvent.OnCategoriaChange -> _state.update { it.copy(categoria = event.categoria) }
            is EditVehiculoUiEvent.OnTransmisionChange -> _state.update { it.copy(transmision = event.transmision) }
            is EditVehiculoUiEvent.OnAsientosChange -> _state.update { it.copy(asientos = event.asientos) }
            is EditVehiculoUiEvent.OnPrecioChange -> { _state.update { it.copy(precioPorDia = event.precio) }; validateForm() }
            is EditVehiculoUiEvent.OnImagenUrlChange -> _state.update { it.copy(imagenUrl = event.url) }
            EditVehiculoUiEvent.GuardarCambios -> guardarCambios()
            EditVehiculoUiEvent.EliminarVehiculo -> eliminarVehiculo()
            else -> Unit
        }
    }
    private fun validateForm() {
        val s = _state.value
        val isValid = s.modelo.isNotBlank() && s.descripcion.isNotBlank() && s.precioPorDia.isNotBlank() && (s.precioPorDia.toDoubleOrNull() ?: 0.0) > 0
        _state.update { it.copy(isFormValid = isValid) }
    }

    private fun loadVehiculo(vehiculoId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val vehiculo = getVehicleDetailUseCase(vehiculoId)
            vehiculo?.let { v ->
                _state.update {
                    it.copy(
                        vehiculoId = v.id, modelo = v.modelo, descripcion = v.descripcion,
                        precioPorDia = v.precioPorDia.toString(), imagenUrl = v.imagenUrl,
                        categoria = v.categoria.name, asientos = v.asientos, transmision = v.transmision.name,
                        isLoading = false, isFormValid = true
                    )
                }
            }
        }
    }
    private fun guardarCambios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = updateVehicleUseCase(
                id = _state.value.vehiculoId, modelo = _state.value.modelo, descripcion = _state.value.descripcion,
                categoria = _state.value.categoria, asientos = _state.value.asientos, transmision = _state.value.transmision,
                precioPorDia = _state.value.precioPorDia.toDoubleOrNull() ?: 0.0, imagenUrl = _state.value.imagenUrl
            )
            when (result) {
                is Resource.Success<*> -> _state.update { it.copy(isLoading = false, saveSuccess = true) }
                is Resource.Error<*> -> _state.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading<*> -> {}
            }
        }
    }
    private fun eliminarVehiculo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = deleteVehicleUseCase(_state.value.vehiculoId)
            when (result) {
                is Resource.Success<*> -> _state.update { it.copy(isLoading = false, deleteSuccess = true) }
                is Resource.Error<*> -> _state.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading<*> -> {}
            }
        }
    }
}
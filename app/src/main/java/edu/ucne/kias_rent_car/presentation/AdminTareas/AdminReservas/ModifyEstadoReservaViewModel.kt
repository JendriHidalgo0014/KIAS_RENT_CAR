package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservacionByIdUseCase
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.UpdateEstadoReservacionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyEstadoReservaViewModel @Inject constructor(
    private val getReservacionByIdUseCase: GetReservacionByIdUseCase,
    private val updateEstadoReservacionUseCase: UpdateEstadoReservacionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ModifyEstadoReservaUiState())
    val state: StateFlow<ModifyEstadoReservaUiState> = _state.asStateFlow()

    fun onEvent(event: ModifyEstadoReservaUiEvent) {
        when (event) {
            is ModifyEstadoReservaUiEvent.LoadReservacion -> loadReservacion(event.reservacionId)
            is ModifyEstadoReservaUiEvent.OnEstadoChange -> _state.update { it.copy(estadoSeleccionado = event.estado) }
            ModifyEstadoReservaUiEvent.GuardarCambios -> guardarCambios()
            else -> Unit
        }
    }

    private fun loadReservacion(reservacionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getReservacionByIdUseCase(reservacionId)) {
                is Resource.Success -> {
                    val reservacion = result.data
                    _state.update {
                        it.copy(
                            reservacionId = reservacion.id,
                            codigoReserva = reservacion.codigoReserva,
                            nombreCliente = reservacion.usuario?.nombre ?: "",
                            vehiculo = reservacion.vehiculo?.modelo ?: "",
                            periodo = "${reservacion.fechaRecogida} - ${reservacion.fechaDevolucion}",
                            estadoSeleccionado = reservacion.estado,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun guardarCambios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = updateEstadoReservacionUseCase(
                reservacionId = _state.value.reservacionId,
                nuevoEstado = _state.value.estadoSeleccionado
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            saveSuccess = true
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
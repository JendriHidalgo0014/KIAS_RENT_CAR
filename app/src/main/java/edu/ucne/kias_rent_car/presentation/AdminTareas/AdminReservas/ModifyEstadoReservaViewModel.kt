package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private fun loadReservacion(reservacionId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val reservacion = getReservacionByIdUseCase(reservacionId)

            _state.update {
                it.copy(
                    reservacionId = reservacion?.reservacionId ?: 0,
                    codigoReserva = reservacion?.codigoReserva ?: "",
                    nombreCliente = reservacion?.usuario?.nombre ?: "",
                    vehiculo = reservacion?.vehiculo?.modelo ?: "",
                    periodo = "${reservacion?.fechaRecogida ?: ""} - ${reservacion?.fechaDevolucion ?: ""}",
                    estadoSeleccionado = reservacion?.estado ?: "",
                    isLoading = false
                )
            }
        }
    }

    private fun guardarCambios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            updateEstadoReservacionUseCase(
                reservacionId = _state.value.reservacionId,
                nuevoEstado = _state.value.estadoSeleccionado
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    saveSuccess = true
                )
            }
        }
    }
}
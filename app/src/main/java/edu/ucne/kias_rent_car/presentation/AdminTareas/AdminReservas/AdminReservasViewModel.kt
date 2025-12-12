package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.model.EstadoReserva
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetAllReservacionesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminReservasViewModel @Inject constructor(
    private val getAllReservacionesUseCase: GetAllReservacionesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AdminReservasUiState())
    val state: StateFlow<AdminReservasUiState> = _state.asStateFlow()

    init {
        loadReservaciones()
    }

    fun onEvent(event: AdminReservasEvent) {
        when (event) {
            is AdminReservasEvent.FiltroChanged -> {
                _state.update { it.copy(filtroActual = event.filtro) }
                loadReservaciones()
            }
            is AdminReservasEvent.Refresh -> loadReservaciones()
            else -> Unit
        }
    }

    private fun loadReservaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val todas = getAllReservacionesUseCase()
            val filtradas = when (_state.value.filtroActual) {
                EstadoReserva.TODOS -> todas
                EstadoReserva.RESERVADO -> todas.filter {
                    it.estado in listOf(EstadoReserva.RESERVADO, EstadoReserva.CONFIRMADA)
                }
                EstadoReserva.EN_PROCESO -> todas.filter {
                    it.estado in listOf(EstadoReserva.EN_PROCESO, EstadoReserva.EN_PROCESO_SIN_ESPACIO)
                }
                EstadoReserva.FINALIZADO -> todas.filter {
                    it.estado in listOf(EstadoReserva.FINALIZADA, EstadoReserva.CANCELADA)
                }
                else -> todas
            }

            _state.update {
                it.copy(
                    reservaciones = filtradas,
                    isLoading = false
                )
            }
        }
    }
}
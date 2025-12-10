package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.model.Reservacion
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
    private var todasLasReservaciones = listOf<Reservacion>()

    init {
        loadReservaciones()
    }
    private fun loadReservaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            todasLasReservaciones = getAllReservacionesUseCase()

            _state.update {
                it.copy(
                    reservaciones = todasLasReservaciones,
                    isLoading = false
                )
            }
        }
    }
    fun onFiltroChanged(filtro: String) {
        _state.update { it.copy(filtroActual = filtro) }

        val filtradas = when (filtro) {
            "Todos" -> todasLasReservaciones
            "Reservado" -> todasLasReservaciones.filter { it.estado == "Confirmada" || it.estado == "Reservado" }
            "En Proceso" -> todasLasReservaciones.filter { it.estado == "EnProceso" || it.estado == "En Proceso" }
            "Finalizado" -> todasLasReservaciones.filter { it.estado == "Finalizada" }
            else -> todasLasReservaciones
        }

        _state.update { it.copy(reservaciones = filtradas) }
    }
}
package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservacionesUsuarioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val getReservacionesUsuarioUseCase: GetReservacionesUsuarioUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookingsUiState())
    val state: StateFlow<BookingsUiState> = _state.asStateFlow()

    init {
        loadReservaciones()
    }

    fun onEvent(event: BookingsEvent) {
        when (event) {
            is BookingsEvent.FilterChanged -> {
                _state.update { it.copy(filtro = event.filter) }
                loadReservaciones()
            }
            is BookingsEvent.Refresh -> loadReservaciones()
        }
    }

    private fun loadReservaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val reservaciones = getReservacionesUsuarioUseCase()
            val filtradas = when (_state.value.filtro) {
                BookingFilter.ACTUALES -> reservaciones.filter {
                    it.estado in listOf("Confirmada", "Pendiente", "EnProceso")
                }
                BookingFilter.PASADAS -> reservaciones.filter {
                    it.estado in listOf("Finalizada", "Finalizado", "Completada", "Completado", "Cancelada", "Cancelado")
                }
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
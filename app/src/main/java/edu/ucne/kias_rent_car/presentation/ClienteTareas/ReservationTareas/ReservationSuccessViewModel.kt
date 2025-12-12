package edu.ucne.kias_rent_car.presentation.ReservationTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservacionByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationSuccessViewModel @Inject constructor(
    private val getReservacionByIdUseCase: GetReservacionByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ReservationSuccessUiState())
    val state: StateFlow<ReservationSuccessUiState> = _state.asStateFlow()

    fun onEvent(event: ReservationSuccessUiEvent) {
        when (event) {
            is ReservationSuccessUiEvent.LoadReservacion -> loadReservacion(event.reservacionId)
            else -> Unit
        }
    }

    private fun loadReservacion(reservacionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val reservacion = getReservacionByIdUseCase(reservacionId.toIntOrNull() ?: 0)
            _state.update {
                it.copy(
                    codigoReserva = reservacion?.codigoReserva ?: "KR-XXXXXX",
                    isLoading = false
                )
            }
        }
    }
}
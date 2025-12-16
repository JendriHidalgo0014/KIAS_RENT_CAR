package edu.ucne.kias_rent_car.presentation.ReservationTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
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

    private val codigoDefecto = "KR-XXXXXX"

    fun onEvent(event: ReservationSuccessUiEvent) {
        when (event) {
            is ReservationSuccessUiEvent.LoadReservacion -> loadReservacion(event.reservacionId)
            else -> Unit
        }
    }

    private fun loadReservacion(reservacionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getReservacionByIdUseCase(reservacionId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            codigoReserva = result.data.codigoReserva,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            codigoReserva = codigoDefecto,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
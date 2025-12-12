package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.CancelReservacionUseCase
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservacionByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel @Inject constructor(
    private val getReservacionByIdUseCase: GetReservacionByIdUseCase,
    private val cancelReservacionUseCase: CancelReservacionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookingDetailUiState())
    val state: StateFlow<BookingDetailUiState> = _state.asStateFlow()

    fun onEvent(event: BookingDetailUiEvent) {
        when (event) {
            is BookingDetailUiEvent.LoadBooking -> loadBooking(event.bookingId)
            BookingDetailUiEvent.CancelarReserva -> cancelarReserva()
            else -> Unit
        }
    }

    private fun loadBooking(bookingId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val reservacion = getReservacionByIdUseCase(bookingId)
            _state.update { it.copy(reservacion = reservacion, isLoading = false) }
        }
    }

    private fun cancelarReserva() {
        viewModelScope.launch {
            _state.value.reservacion?.let { reservacion ->
                cancelReservacionUseCase(reservacion.reservacionId)
                _state.update { it.copy(cancelSuccess = true) }
                loadBooking(reservacion.reservacionId)
            }
        }
    }
}
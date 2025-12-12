package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservacionByIdUseCase
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.UpdateReservacionUseCase
import edu.ucne.kias_rent_car.domain.usecase.Ubicacion.GetUbicacionesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ModifyBookingViewModel @Inject constructor(
    private val getReservacionByIdUseCase: GetReservacionByIdUseCase,
    private val updateReservacionUseCase: UpdateReservacionUseCase,
    private val getUbicacionesUseCase: GetUbicacionesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ModifyBookingUiState())
    val state: StateFlow<ModifyBookingUiState> = _state.asStateFlow()

    fun onEvent(event: ModifyBookingUiEvent) {
        when (event) {
            is ModifyBookingUiEvent.LoadBooking -> loadBooking(event.bookingId)
            is ModifyBookingUiEvent.OnUbicacionRecogidaChange -> _state.update { it.copy(ubicacionRecogida = event.ubicacion, error = null) }
            is ModifyBookingUiEvent.OnUbicacionDevolucionChange -> _state.update { it.copy(ubicacionDevolucion = event.ubicacion, error = null) }
            is ModifyBookingUiEvent.OnFechaRecogidaChange -> onFechaRecogidaChanged(event.fecha, event.hora)
            is ModifyBookingUiEvent.OnFechaDevolucionChange -> onFechaDevolucionChanged(event.fecha, event.hora)
            ModifyBookingUiEvent.GuardarCambios -> guardarCambios()
            else -> Unit
        }
    }

    private fun loadBooking(bookingId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val ubicaciones = getUbicacionesUseCase()
            val reservacion = getReservacionByIdUseCase(bookingId)

            reservacion?.let { r ->
                _state.update {
                    it.copy(
                        bookingId = r.reservacionId,
                        ubicaciones = ubicaciones,
                        ubicacionRecogida = r.ubicacionRecogida,
                        ubicacionDevolucion = r.ubicacionDevolucion,
                        fechaRecogida = "${r.fechaRecogida} - ${r.horaRecogida}",
                        fechaDevolucion = "${r.fechaDevolucion} - ${r.horaDevolucion}",
                        fechaRecogidaDate = r.fechaRecogida,
                        horaRecogidaTime = r.horaRecogida,
                        fechaDevolucionDate = r.fechaDevolucion,
                        horaDevolucionTime = r.horaDevolucion,
                        isLoading = false
                    )
                }
            } ?: run {
                _state.update { it.copy(isLoading = false, error = "No se encontró la reservación") }
            }
        }
    }

    private fun onFechaRecogidaChanged(fecha: LocalDate, hora: LocalTime) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        _state.update {
            it.copy(
                fechaRecogida = "${fecha.format(dateFormatter)} - ${hora.format(timeFormatter)}",
                fechaRecogidaDate = fecha.format(dateFormatter),
                horaRecogidaTime = hora.format(timeFormatter),
                error = null
            )
        }
    }

    private fun onFechaDevolucionChanged(fecha: LocalDate, hora: LocalTime) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        _state.update {
            it.copy(
                fechaDevolucion = "${fecha.format(dateFormatter)} - ${hora.format(timeFormatter)}",
                fechaDevolucionDate = fecha.format(dateFormatter),
                horaDevolucionTime = hora.format(timeFormatter),
                error = null
            )
        }
    }

    private fun guardarCambios() {
        viewModelScope.launch {
            val s = _state.value

            when {
                s.ubicacionRecogida == null -> { _state.update { it.copy(error = "Selecciona el lugar de recogida") }; return@launch }
                s.ubicacionDevolucion == null -> { _state.update { it.copy(error = "Selecciona el lugar de devolución") }; return@launch }
                s.fechaRecogidaDate.isEmpty() -> { _state.update { it.copy(error = "Selecciona la fecha de recogida") }; return@launch }
                s.fechaDevolucionDate.isEmpty() -> { _state.update { it.copy(error = "Selecciona la fecha de devolución") }; return@launch }
            }

            _state.update { it.copy(isLoading = true, error = null) }

            val result = updateReservacionUseCase(
                reservacionId = s.bookingId,
                ubicacionRecogidaId = s.ubicacionRecogida!!.ubicacionId,
                ubicacionDevolucionId = s.ubicacionDevolucion!!.ubicacionId,
                fechaRecogida = s.fechaRecogidaDate,
                horaRecogida = s.horaRecogidaTime,
                fechaDevolucion = s.fechaDevolucionDate,
                horaDevolucion = s.horaDevolucionTime
            )

            when (result) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, saveSuccess = true) }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message ?: "Error al guardar") }
                is Resource.Loading -> {}
            }
        }
    }
}
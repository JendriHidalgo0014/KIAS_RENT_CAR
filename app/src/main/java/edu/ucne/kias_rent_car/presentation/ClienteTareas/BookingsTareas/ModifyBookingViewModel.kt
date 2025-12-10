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

    fun loadBooking(bookingId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Cargar ubicaciones
            val ubicaciones = getUbicacionesUseCase()

            // Cargar reservación
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

    fun onUbicacionRecogidaChanged(ubicacion: Ubicacion) {
        _state.update { it.copy(ubicacionRecogida = ubicacion, error = null) }
    }

    fun onUbicacionDevolucionChanged(ubicacion: Ubicacion) {
        _state.update { it.copy(ubicacionDevolucion = ubicacion, error = null) }
    }

    fun onFechaRecogidaChanged(fecha: LocalDate, hora: LocalTime) {
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

    fun onFechaDevolucionChanged(fecha: LocalDate, hora: LocalTime) {
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

    fun onLugarRecogidaChanged(value: String) {}
    fun onLugarDevolucionChanged(value: String) {}

    fun guardarCambios() {
        viewModelScope.launch {
            val currentState = _state.value

            // Validaciones
            if (currentState.ubicacionRecogida == null) {
                _state.update { it.copy(error = "Selecciona el lugar de recogida") }
                return@launch
            }
            if (currentState.ubicacionDevolucion == null) {
                _state.update { it.copy(error = "Selecciona el lugar de devolución") }
                return@launch
            }
            if (currentState.fechaRecogidaDate.isEmpty()) {
                _state.update { it.copy(error = "Selecciona la fecha de recogida") }
                return@launch
            }
            if (currentState.fechaDevolucionDate.isEmpty()) {
                _state.update { it.copy(error = "Selecciona la fecha de devolución") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }

            // Llamar al use case para actualizar
            val result = updateReservacionUseCase(
                reservacionId = currentState.bookingId,
                ubicacionRecogidaId = currentState.ubicacionRecogida.ubicacionId,
                ubicacionDevolucionId = currentState.ubicacionDevolucion.ubicacionId,
                fechaRecogida = currentState.fechaRecogidaDate,
                horaRecogida = currentState.horaRecogidaTime,
                fechaDevolucion = currentState.fechaDevolucionDate,
                horaDevolucion = currentState.horaDevolucionTime
            )

            when (result) {
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
                            error = result.message ?: "Error al guardar"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
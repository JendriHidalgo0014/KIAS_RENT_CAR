package edu.ucne.kias_rent_car.presentation.ClienteTareas.UbicacionTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.SaveReservationConfigUseCase
import edu.ucne.kias_rent_car.domain.usecase.Ubicacion.GetUbicacionesUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetVehicleDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ReservationConfigViewModel @Inject constructor(
    private val getUbicacionesUseCase: GetUbicacionesUseCase,
    private val getVehicleDetailUseCase: GetVehicleDetailUseCase,
    private val saveReservationConfigUseCase: SaveReservationConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReservationConfigUiState())
    val state: StateFlow<ReservationConfigUiState> = _state.asStateFlow()

    fun init(vehicleId: String) {
        _state.update { it.copy(vehicleId = vehicleId) }
        loadUbicaciones()
        loadVehicle(vehicleId)
    }

    private fun loadVehicle(vehicleId: String) {
        viewModelScope.launch {
            try {
                val vehicle = getVehicleDetailUseCase(vehicleId)
                if (vehicle != null) {
                    _state.update { it.copy(precioPorDia = vehicle.precioPorDia) }
                }
            } catch (e: Exception) {
                // Silenciar error
            }
        }
    }

    private fun loadUbicaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val ubicaciones = getUbicacionesUseCase()
                _state.update {
                    it.copy(
                        ubicaciones = ubicaciones,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar ubicaciones"
                    )
                }
            }
        }
    }

    fun onEvent(event: ReservationConfigEvent) {
        when (event) {
            is ReservationConfigEvent.LugarRecogidaChanged -> {
                _state.update { it.copy(lugarRecogida = event.ubicacion, error = null) }
            }
            is ReservationConfigEvent.LugarDevolucionChanged -> {
                _state.update { it.copy(lugarDevolucion = event.ubicacion, error = null) }
            }
            is ReservationConfigEvent.FechaRecogidaChanged -> {
                _state.update { it.copy(fechaRecogida = event.fecha, error = null) }
                calculatePrice()
            }
            is ReservationConfigEvent.HoraRecogidaChanged -> {
                _state.update { it.copy(horaRecogida = event.hora, error = null) }
            }
            is ReservationConfigEvent.FechaDevolucionChanged -> {
                _state.update { it.copy(fechaDevolucion = event.fecha, error = null) }
                calculatePrice()
            }
            is ReservationConfigEvent.HoraDevolucionChanged -> {
                _state.update { it.copy(horaDevolucion = event.hora, error = null) }
            }
            is ReservationConfigEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun calculatePrice() {
        val currentState = _state.value
        val fechaRecogida = currentState.fechaRecogida
        val fechaDevolucion = currentState.fechaDevolucion

        if (fechaRecogida != null && fechaDevolucion != null) {
            val dias = ChronoUnit.DAYS.between(fechaRecogida, fechaDevolucion).toInt().coerceAtLeast(1)
            val subtotal = currentState.precioPorDia * dias
            val impuestos = subtotal * 0.18
            val total = subtotal + impuestos

            _state.update {
                it.copy(
                    dias = dias,
                    subtotal = subtotal,
                    impuestos = impuestos,
                    total = total
                )
            }
        }
    }

    fun validateForm(): Boolean {
        val currentState = _state.value

        if (currentState.lugarRecogida == null) {
            _state.update { it.copy(error = "Selecciona el lugar de recogida") }
            return false
        }
        if (currentState.lugarDevolucion == null) {
            _state.update { it.copy(error = "Selecciona el lugar de devolución") }
            return false
        }
        if (currentState.fechaRecogida == null || currentState.horaRecogida == null) {
            _state.update { it.copy(error = "Selecciona la fecha y hora de recogida") }
            return false
        }
        if (currentState.fechaDevolucion == null || currentState.horaDevolucion == null) {
            _state.update { it.copy(error = "Selecciona la fecha y hora de devolución") }
            return false
        }
        if (currentState.fechaDevolucion < currentState.fechaRecogida) {
            _state.update { it.copy(error = "La fecha de devolución debe ser posterior a la de recogida") }
            return false
        }

        return true
    }

    fun saveConfig() {
        val currentState = _state.value
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        viewModelScope.launch {
            val config = ReservationConfig(
                vehicleId = currentState.vehicleId,
                lugarRecogida = currentState.lugarRecogida?.nombre ?: "",
                lugarDevolucion = currentState.lugarDevolucion?.nombre ?: "",
                fechaRecogida = currentState.fechaRecogida?.format(dateFormatter) ?: "",
                fechaDevolucion = currentState.fechaDevolucion?.format(dateFormatter) ?: "",
                horaRecogida = currentState.horaRecogida?.format(timeFormatter) ?: "10:00",
                horaDevolucion = currentState.horaDevolucion?.format(timeFormatter) ?: "10:00",
                dias = currentState.dias,
                subtotal = currentState.subtotal,
                impuestos = currentState.impuestos,
                total = currentState.total,
                ubicacionRecogidaId = currentState.lugarRecogida?.ubicacionId ?: 0,
                ubicacionDevolucionId = currentState.lugarDevolucion?.ubicacionId ?: 0
            )

            saveReservationConfigUseCase(config)
        }
    }
}
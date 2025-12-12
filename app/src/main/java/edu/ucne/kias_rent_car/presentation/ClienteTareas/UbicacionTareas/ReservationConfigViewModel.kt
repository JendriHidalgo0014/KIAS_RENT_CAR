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

    fun onEvent(event: ReservationConfigUiEvent) {
        when (event) {
            is ReservationConfigUiEvent.Init -> init(event.vehicleId)
            is ReservationConfigUiEvent.OnLugarRecogidaChange -> {
                _state.update { it.copy(lugarRecogida = event.ubicacion, error = null) }
                validateForm()
            }
            is ReservationConfigUiEvent.OnLugarDevolucionChange -> {
                _state.update { it.copy(lugarDevolucion = event.ubicacion, error = null) }
                validateForm()
            }
            is ReservationConfigUiEvent.OnFechaRecogidaChange -> {
                _state.update { it.copy(fechaRecogida = event.fecha, error = null) }
                calculatePrice()
                validateForm()
            }
            is ReservationConfigUiEvent.OnHoraRecogidaChange -> {
                _state.update { it.copy(horaRecogida = event.hora, error = null) }
                validateForm()
            }
            is ReservationConfigUiEvent.OnFechaDevolucionChange -> {
                _state.update { it.copy(fechaDevolucion = event.fecha, error = null) }
                calculatePrice()
                validateForm()
            }
            is ReservationConfigUiEvent.OnHoraDevolucionChange -> {
                _state.update { it.copy(horaDevolucion = event.hora, error = null) }
                validateForm()
            }
            ReservationConfigUiEvent.Continuar -> saveConfig()
            else -> Unit
        }
    }

    private fun init(vehicleId: String) {
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
            } catch (_: Exception) { }
        }
    }

    private fun loadUbicaciones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val ubicaciones = getUbicacionesUseCase()
                _state.update { it.copy(ubicaciones = ubicaciones, isLoading = false) }
            } catch (_: Exception) {
                _state.update { it.copy(isLoading = false, error = "Error al cargar ubicaciones") }
            }
        }
    }

    private fun calculatePrice() {
        val s = _state.value
        val fechaRecogida = s.fechaRecogida
        val fechaDevolucion = s.fechaDevolucion

        if (fechaRecogida != null && fechaDevolucion != null) {
            val dias = ChronoUnit.DAYS.between(fechaRecogida, fechaDevolucion).toInt().coerceAtLeast(1)
            val subtotal = s.precioPorDia * dias
            val impuestos = subtotal * 0.18
            val total = subtotal + impuestos
            _state.update { it.copy(dias = dias, subtotal = subtotal, impuestos = impuestos, total = total) }
        }
    }

    private fun validateForm() {
        val s = _state.value
        val isValid = s.lugarRecogida != null &&
                s.lugarDevolucion != null &&
                s.fechaRecogida != null &&
                s.horaRecogida != null &&
                s.fechaDevolucion != null &&
                s.horaDevolucion != null &&
                (s.fechaDevolucion >= s.fechaRecogida)
        _state.update { it.copy(isFormValid = isValid) }
    }

    private fun saveConfig() {
        val s = _state.value
        if (!s.isFormValid) {
            _state.update { it.copy(error = "Completa todos los campos") }
            return
        }

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        viewModelScope.launch {
            val config = ReservationConfig(
                vehicleId = s.vehicleId,
                lugarRecogida = s.lugarRecogida?.nombre ?: "",
                lugarDevolucion = s.lugarDevolucion?.nombre ?: "",
                fechaRecogida = s.fechaRecogida?.format(dateFormatter) ?: "",
                fechaDevolucion = s.fechaDevolucion?.format(dateFormatter) ?: "",
                horaRecogida = s.horaRecogida?.format(timeFormatter) ?: "10:00",
                horaDevolucion = s.horaDevolucion?.format(timeFormatter) ?: "10:00",
                dias = s.dias,
                subtotal = s.subtotal,
                impuestos = s.impuestos,
                total = s.total,
                ubicacionRecogidaId = s.lugarRecogida?.ubicacionId ?: 0,
                ubicacionDevolucionId = s.lugarDevolucion?.ubicacionId ?: 0
            )
            saveReservationConfigUseCase(config)
            _state.update { it.copy(configSaved = true) }
        }
    }
}
package edu.ucne.kias_rent_car.presentation.ReservationTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservationConfigUseCase
import edu.ucne.kias_rent_car.domain.usecase.Vehicle.GetVehicleDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationConfirmationViewModel @Inject constructor(
    private val getVehicleDetailUseCase: GetVehicleDetailUseCase,
    private val getReservationConfigUseCase: GetReservationConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReservationConfirmationUiState())
    val state: StateFlow<ReservationConfirmationUiState> = _state.asStateFlow()

    fun onEvent(event: ReservationConfirmationUiEvent) {
        when (event) {
            is ReservationConfirmationUiEvent.LoadData -> loadConfirmationData(event.vehicleId)
            else -> Unit
        }
    }

    private fun loadConfirmationData(vehicleId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val vehicleResult = getVehicleDetailUseCase(vehicleId)
            val configResult = getReservationConfigUseCase()

            if (vehicleResult is Resource.Success && configResult is Resource.Success) {
                val vehicle = vehicleResult.data
                val config = configResult.data

                if (config != null) {
                    val dias = config.dias
                    val subtotal = vehicle.precioPorDia * dias
                    val impuestos = subtotal * 0.18
                    val total = subtotal + impuestos

                    _state.update {
                        it.copy(
                            vehicle = vehicle,
                            fechaRecogida = config.fechaRecogida,
                            fechaDevolucion = config.fechaDevolucion,
                            lugarRecogida = config.lugarRecogida,
                            lugarDevolucion = config.lugarDevolucion,
                            dias = dias,
                            subtotal = subtotal,
                            impuestos = impuestos,
                            total = total,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false) }
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
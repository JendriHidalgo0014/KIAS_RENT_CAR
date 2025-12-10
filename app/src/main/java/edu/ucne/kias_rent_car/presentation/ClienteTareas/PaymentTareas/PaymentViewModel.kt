package edu.ucne.kias_rent_car.presentation.PaymentTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.CreateReservacionUseCase
import edu.ucne.kias_rent_car.domain.usecase.Reservacion.GetReservationConfigUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createReservacionUseCase: CreateReservacionUseCase,
    private val getReservationConfigUseCase: GetReservationConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentUiState())
    val state: StateFlow<PaymentUiState> = _state.asStateFlow()

    fun init(vehicleId: String) {
        _state.update { it.copy(vehicleId = vehicleId) }
        loadReservationConfig()
    }

    private fun loadReservationConfig() {
        viewModelScope.launch {
            val config = getReservationConfigUseCase()
            config?.let { cfg ->
                _state.update { state ->
                    state.copy(total = cfg.total)
                }
            }
        }
    }

    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.MetodoPagoChanged -> {
                _state.update { it.copy(metodoPago = event.metodo) }
            }
            is PaymentEvent.NumeroTarjetaChanged -> {
                _state.update { it.copy(numeroTarjeta = event.numero.take(16)) }
            }
            is PaymentEvent.VencimientoChanged -> {
                _state.update { it.copy(vencimiento = event.vencimiento.take(5)) }
            }
            is PaymentEvent.CvvChanged -> {
                _state.update { it.copy(cvv = event.cvv.take(4)) }
            }
            is PaymentEvent.NombreTitularChanged -> {
                _state.update { it.copy(nombreTitular = event.nombre) }
            }
            is PaymentEvent.GuardarTarjetaChanged -> {
                _state.update { it.copy(guardarTarjeta = event.guardar) }
            }
            is PaymentEvent.ProcesarPago -> procesarPago()
        }
    }

    fun procesarPago() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = createReservacionUseCase()) {
                is Resource.Success -> {
                    val reservacion = result.data
                    _state.update {
                        it.copy(
                            isLoading = false,
                            paymentSuccess = true,
                            // ✅ SOLO EL ID, NO TODO EL OBJETO
                            reservacionId = reservacion?.reservacionId?.toString() ?: "0"
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
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
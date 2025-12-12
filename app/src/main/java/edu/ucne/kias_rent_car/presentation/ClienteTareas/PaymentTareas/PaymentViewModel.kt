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

    fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.Init -> init(event.vehicleId)
            is PaymentUiEvent.OnMetodoPagoChange -> {
                _state.update { it.copy(metodoPago = event.metodo) }
                validateForm()
            }
            is PaymentUiEvent.OnNumeroTarjetaChange -> {
                _state.update { it.copy(numeroTarjeta = event.numero.filter { c -> c.isDigit() }.take(16)) }
                validateForm()
            }
            is PaymentUiEvent.OnVencimientoChange -> {
                _state.update { it.copy(vencimiento = event.vencimiento.filter { c -> c.isDigit() }.take(4)) }
                validateForm()
            }
            is PaymentUiEvent.OnCvvChange -> {
                _state.update { it.copy(cvv = event.cvv.filter { c -> c.isDigit() }.take(4)) }
                validateForm()
            }
            is PaymentUiEvent.OnNombreTitularChange -> {
                _state.update { it.copy(nombreTitular = event.nombre.filter { c -> c.isLetter() || c == ' ' }.take(30)) }
                validateForm()
            }
            is PaymentUiEvent.OnGuardarTarjetaChange -> _state.update { it.copy(guardarTarjeta = event.guardar) }
            PaymentUiEvent.ProcesarPago -> procesarPago()
            else -> Unit
        }
    }

    private fun init(vehicleId: String) {
        _state.update { it.copy(vehicleId = vehicleId) }
        loadReservationConfig()
    }

    private fun loadReservationConfig() {
        viewModelScope.launch {
            val config = getReservationConfigUseCase()
            config?.let { cfg ->
                _state.update { it.copy(total = cfg.total) }
            }
        }
    }

    private fun validateForm() {
        val s = _state.value
        val isValid = when (s.metodoPago) {
            MetodoPago.TARJETA -> s.numeroTarjeta.length >= 16 &&
                    s.vencimiento.length >= 4 &&
                    s.cvv.length >= 3 &&
                    s.nombreTitular.isNotBlank()
            MetodoPago.BILLETERA -> true
        }
        _state.update { it.copy(isFormValid = isValid) }
    }

    private fun procesarPago() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = createReservacionUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            paymentSuccess = true,
                            reservacionId = result.data?.reservacionId?.toString() ?: "0"
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
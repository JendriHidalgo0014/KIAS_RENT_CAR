package edu.ucne.kias_rent_car.presentation.SupportTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Mensaje.SendMensajeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val sendMensajeUseCase: SendMensajeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SendMessageUiState())
    val state: StateFlow<SendMessageUiState> = _state.asStateFlow()

    fun onAsuntoChanged(value: String) {
        _state.update { it.copy(asunto = value) }
    }

    fun onMensajeChanged(value: String) {
        _state.update { it.copy(mensaje = value) }
    }

    fun enviarMensaje() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            sendMensajeUseCase(
                asunto = _state.value.asunto,
                mensaje = _state.value.mensaje
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    messageSent = true
                )
            }
        }
    }
}
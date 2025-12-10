package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Mensaje.GetMensajeByIdUseCase
import edu.ucne.kias_rent_car.domain.usecase.Mensaje.ResponderMensajeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResponderMensajeViewModel @Inject constructor(
    private val getMensajeByIdUseCase: GetMensajeByIdUseCase,
    private val responderMensajeUseCase: ResponderMensajeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ResponderMensajeUiState())
    val state: StateFlow<ResponderMensajeUiState> = _state.asStateFlow()
    fun loadMensaje(mensajeId: Int) {
        viewModelScope.launch {
            val mensaje = getMensajeByIdUseCase(mensajeId)

            mensaje?.let { m ->
                _state.update {
                    it.copy(
                        mensajeId = m.mensajeId,
                        nombreUsuario = m.nombreUsuario,
                        mensajeOriginal = m.contenido
                    )
                }
            }
        }
    }

    fun onRespuestaChanged(value: String) {
        _state.update { it.copy(respuesta = value) }
    }
    fun enviarRespuesta() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            responderMensajeUseCase(
                mensajeId = _state.value.mensajeId,
                respuesta = _state.value.respuesta
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    enviado = true
                )
            }
        }
    }
}
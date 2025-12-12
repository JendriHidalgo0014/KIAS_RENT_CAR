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

    fun onEvent(event: ResponderMensajeUiEvent) {
        when (event) {
            is ResponderMensajeUiEvent.LoadMensaje -> loadMensaje(event.mensajeId)
            is ResponderMensajeUiEvent.OnRespuestaChange -> _state.update { it.copy(respuesta = event.respuesta) }
            ResponderMensajeUiEvent.EnviarRespuesta -> enviarRespuesta()
            else -> Unit
        }
    }

    private fun loadMensaje(mensajeId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val mensaje = getMensajeByIdUseCase(mensajeId)

            _state.update {
                it.copy(
                    mensajeId = mensaje?.mensajeId ?: 0,
                    nombreUsuario = mensaje?.nombreUsuario ?: "",
                    mensajeOriginal = mensaje?.contenido ?: "",
                    isLoading = false
                )
            }
        }
    }

    private fun enviarRespuesta() {
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
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
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
    private fun loadMensaje(mensajeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getMensajeByIdUseCase(mensajeId)) {
                is Resource.Success -> {
                    val mensaje = result.data
                    _state.update {
                        it.copy(
                            mensajeId = mensaje.id,
                            nombreUsuario = mensaje.nombreUsuario,
                            mensajeOriginal = mensaje.contenido,
                            isLoading = false
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
                is Resource.Loading -> Unit
            }
        }
    }
    private fun enviarRespuesta() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = responderMensajeUseCase(
                mensajeId = _state.value.mensajeId,
                respuesta = _state.value.respuesta
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            enviado = true
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
                is Resource.Loading -> Unit
            }
        }
    }
}
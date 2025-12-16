package edu.ucne.kias_rent_car.presentation.SupportTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Mensaje.GetMensajeByIdUseCase
import edu.ucne.kias_rent_car.domain.usecase.Usuario.GetUsuarioLogueadoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val getMensajeByIdUseCase: GetMensajeByIdUseCase,
    private val getUsuarioLogueadoUseCase: GetUsuarioLogueadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SupportUiState())
    val state: StateFlow<SupportUiState> = _state.asStateFlow()

    fun onEvent(event: SupportUiEvent) {
        when (event) {
            SupportUiEvent.LoadMensajes -> loadMensajes()
            else -> Unit
        }
    }

    private fun loadMensajes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val usuarioResult = getUsuarioLogueadoUseCase()) {
                is Resource.Success -> {
                    val usuario = usuarioResult.data
                    if (usuario != null) {
                        when (val mensajesResult = getMensajeByIdUseCase(usuario.id)) {
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                    )
                                }
                            }
                            is Resource.Error -> {
                                _state.update { it.copy(isLoading = false) }
                            }
                            is Resource.Loading -> Unit
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
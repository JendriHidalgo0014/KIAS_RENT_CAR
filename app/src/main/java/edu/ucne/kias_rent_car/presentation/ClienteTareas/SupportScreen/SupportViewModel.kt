package edu.ucne.kias_rent_car.presentation.SupportTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SupportUiState())
    val state: StateFlow<SupportUiState> = _state.asStateFlow()

    fun loadMensajes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val usuario = usuarioRepository.getUsuarioLogueado()
            if (usuario != null) {
                val mensajes = mensajeRepository.getMensajesByUsuario(usuario.id)
                _state.update {
                    it.copy(
                        isLoading = false,
                        mensajes = mensajes
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class SupportUiState(
    val isLoading: Boolean = false,
    val mensajes: List<Mensaje> = emptyList()
)
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.usecase.Mensaje.GetAllMensajesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMensajesViewModel @Inject constructor(
    private val getAllMensajesUseCase: GetAllMensajesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AdminMensajesUiState())
    val state: StateFlow<AdminMensajesUiState> = _state.asStateFlow()

    init {
        loadMensajes()
    }

    fun onEvent(event: AdminMensajesUiEvent) {
        when (event) {
            AdminMensajesUiEvent.Refresh -> loadMensajes()
            else -> Unit
        }
    }

    private fun loadMensajes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val mensajes = getAllMensajesUseCase()

            _state.update {
                it.copy(
                    mensajes = mensajes,
                    isLoading = false
                )
            }
        }
    }
}
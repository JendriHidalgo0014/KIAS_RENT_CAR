package edu.ucne.kias_rent_car.presentation.ProfileTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminProfileUiState())
    val state: StateFlow<AdminProfileUiState> = _state.asStateFlow()

    init {
        loadUsuario()
    }

    fun onEvent(event: AdminProfileUiEvent) {
        when (event) {
            AdminProfileUiEvent.Logout -> logout()
            else -> Unit
        }
    }

    private fun loadUsuario() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val usuario = usuarioRepository.getUsuarioLogueado()

            _state.update { state ->
                state.copy(
                    nombre = usuario?.nombre ?: "",
                    email = usuario?.email ?: "",
                    rol = usuario?.rol ?: "",
                    isLoading = false
                )
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            usuarioRepository.logout()
        }
    }
}
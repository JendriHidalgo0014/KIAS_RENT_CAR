package edu.ucne.kias_rent_car.presentation.AdminTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import edu.ucne.kias_rent_car.presentation.ProfileTareas.AdminProfileUiState
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

    private fun loadUsuario() {
        viewModelScope.launch {
            val usuario = usuarioRepository.getUsuarioLogueado()
            usuario?.let {
                _state.update { state ->
                    state.copy(
                        nombre = it.nombre,
                        email = it.email,
                        rol = it.rol
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            usuarioRepository.logout()
        }
    }
}


package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome

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
class AdminHomeViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AdminHomeUiState())
    val state: StateFlow<AdminHomeUiState> = _state.asStateFlow()

    init {
        loadAdmin()
    }

    fun onEvent(event: AdminHomeUiEvent) {
        when (event) {
            else -> Unit
        }
    }

    private fun loadAdmin() {
        viewModelScope.launch {
            val usuario = usuarioRepository.getUsuarioLogueado()
            _state.update {
                it.copy(nombreAdmin = usuario?.nombre ?: "Admin")
            }
        }
    }
}
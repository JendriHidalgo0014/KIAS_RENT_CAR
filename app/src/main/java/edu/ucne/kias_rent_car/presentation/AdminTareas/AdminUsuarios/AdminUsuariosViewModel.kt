package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.usecase.Usuario.DeleteUsuarioUseCase
import edu.ucne.kias_rent_car.domain.usecase.Usuario.GetAllUsuariosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUsuariosViewModel @Inject constructor(
    private val getAllUsuariosUseCase: GetAllUsuariosUseCase,
    private val deleteUsuarioUseCase: DeleteUsuarioUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AdminUsuariosUiState())
    val state: StateFlow<AdminUsuariosUiState> = _state.asStateFlow()
    private var todosLosUsuarios = listOf<Usuario>()

    init {
        loadUsuarios()
    }

    fun onEvent(event: AdminUsuariosUiEvent) {
        when (event) {
            is AdminUsuariosUiEvent.OnSearchChange -> onSearchChanged(event.query)
            is AdminUsuariosUiEvent.DeleteUsuario -> deleteUsuario(event.id)
            else -> Unit
        }
    }

    private fun loadUsuarios() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            todosLosUsuarios = getAllUsuariosUseCase()
            _state.update { it.copy(usuarios = todosLosUsuarios, isLoading = false) }
        }
    }

    private fun onSearchChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        val filtrados = if (query.isBlank()) {
            todosLosUsuarios
        } else {
            todosLosUsuarios.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true)
            }
        }
        _state.update { it.copy(usuarios = filtrados) }
    }

    private fun deleteUsuario(id: Int) {
        viewModelScope.launch {
            deleteUsuarioUseCase(id)
            loadUsuarios()
        }
    }
}
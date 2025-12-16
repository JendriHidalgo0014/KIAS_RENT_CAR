package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Usuario.GetUsuarioLogueadoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val getUsuarioLogueadoUseCase: GetUsuarioLogueadoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminHomeUiState())
    val uiState: StateFlow<AdminHomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AdminHomeUiEvent>()
    val navigationEvent: SharedFlow<AdminHomeUiEvent> = _navigationEvent.asSharedFlow()

    private val nombreDefecto = "Admin"

    init {
        loadAdmin()
    }

    fun onEvent(event: AdminHomeUiEvent) {
        when (event) {
            AdminHomeUiEvent.NavigateToVehiculos,
            AdminHomeUiEvent.NavigateToReservas,
            AdminHomeUiEvent.NavigateToUsuarios,
            AdminHomeUiEvent.NavigateToMensajes,
            AdminHomeUiEvent.NavigateToProfile -> {
                viewModelScope.launch {
                    _navigationEvent.emit(event)
                }
            }
        }
    }

    private fun loadAdmin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getUsuarioLogueadoUseCase()) {
                is Resource.Success -> {
                    val nombre = result.data?.nombre ?: nombreDefecto
                    _uiState.update {
                        it.copy(
                            nombreAdmin = nombre,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            nombreAdmin = nombreDefecto,
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
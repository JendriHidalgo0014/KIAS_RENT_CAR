package edu.ucne.kias_rent_car.presentation.ProfileTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Usuario.GetUsuarioLogueadoUseCase
import edu.ucne.kias_rent_car.domain.usecase.Usuario.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUsuarioLogueadoUseCase: GetUsuarioLogueadoUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.Logout -> logout()
            else -> Unit
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getUsuarioLogueadoUseCase()) {
                is Resource.Success -> {
                    val usuario = result.data
                    _state.update {
                        it.copy(
                            nombre = usuario?.nombre ?: "",
                            email = usuario?.email ?: "",
                            telefono = usuario?.telefono ?: "No registrado",
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _state.update { it.copy(logoutSuccess = true) }
        }
    }
}
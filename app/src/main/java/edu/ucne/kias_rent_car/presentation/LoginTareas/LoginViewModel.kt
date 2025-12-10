package edu.ucne.kias_rent_car.presentation.LoginTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Usuario.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, error = null) }
            }
            is LoginUiEvent.Login -> {
                realizarLogin()
            }
            is LoginUiEvent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
            }
            is LoginUiEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun realizarLogin() {
        val state = _uiState.value

        if (!state.esFormularioValido()) {
            _uiState.update {
                it.copy(error = "Por favor completa todos los campos")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val resultado = loginUseCase(state.email, state.password)

            _uiState.update {
                when (resultado) {
                    is Resource.Success -> {
                        it.copy(
                            isLoading = false,
                            loginExitoso = resultado.data,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        it.copy(
                            isLoading = false,
                            error = resultado.message,
                            loginExitoso = null
                        )
                    }
                    is Resource.Loading -> {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun resetLoginExitoso() {
        _uiState.update { it.copy(loginExitoso = null) }
    }
}
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
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChange -> _state.update { it.copy(email = event.email) }
            is LoginUiEvent.OnPasswordChange -> _state.update { it.copy(password = event.password) }
            LoginUiEvent.TogglePasswordVisibility -> _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            LoginUiEvent.Login -> login()
            LoginUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun esFormularioValido(): Boolean {
        return _state.value.email.isNotBlank() && _state.value.password.isNotBlank()
    }

    private fun login() {
        if (!esFormularioValido()) {
            _state.update { it.copy(userMessage = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = loginUseCase(_state.value.email, _state.value.password)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loginExitoso = result.data
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun resetLoginExitoso() {
        _state.update { it.copy(loginExitoso = null) }
    }
}
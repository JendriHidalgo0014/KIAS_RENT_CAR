package edu.ucne.kias_rent_car.presentation.LoginTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.usecase.Usuario.RegistrarUsuarioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val registrarUsuarioUseCase: RegistrarUsuarioUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegistroUiState())
    val state: StateFlow<RegistroUiState> = _state.asStateFlow()

    // Regex para validar email
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    private fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    fun onEvent(event: RegistroUiEvent) {
        when (event) {
            is RegistroUiEvent.NombreChanged -> {
                _state.update { it.copy(nombre = event.nombre, error = null) }
            }
            is RegistroUiEvent.EmailChanged -> {
                val email = event.email
                val emailError = if (email.isNotBlank() && !isValidEmail(email)) {
                    "Formato inválido (ejemplo@gmail.com)"
                } else {
                    null
                }
                _state.update { it.copy(email = email, emailError = emailError, error = null) }
            }
            is RegistroUiEvent.TelefonoChanged -> {
                _state.update { it.copy(telefono = event.telefono, error = null) }
            }
            is RegistroUiEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, error = null) }
            }
            is RegistroUiEvent.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = event.confirmPassword, error = null) }
            }
            is RegistroUiEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            }
            is RegistroUiEvent.Registrar -> {
                registrar()
            }
            is RegistroUiEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    // Funciones legacy para compatibilidad
    fun updateUserName(userName: String) = onEvent(RegistroUiEvent.NombreChanged(userName))
    fun updatePassword(password: String) = onEvent(RegistroUiEvent.PasswordChanged(password))
    fun updateConfirmPassword(confirmPassword: String) = onEvent(RegistroUiEvent.ConfirmPasswordChanged(confirmPassword))
    fun togglePasswordVisibility() = onEvent(RegistroUiEvent.TogglePasswordVisibility)

    fun registrar() {
        val currentState = _state.value

        if (currentState.nombre.isBlank()) {
            _state.update { it.copy(error = "El nombre es requerido") }
            return
        }

        if (currentState.email.isBlank()) {
            _state.update { it.copy(error = "El email es requerido") }
            return
        }

        if (!isValidEmail(currentState.email)) {
            _state.update { it.copy(error = "Ingresa un email válido (ejemplo@gmail.com)") }
            return
        }

        if (currentState.password.length < 4) {
            _state.update { it.copy(error = "La contraseña debe tener al menos 4 caracteres") }
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            _state.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val resultado = registrarUsuarioUseCase(
                nombre = currentState.nombre,
                email = currentState.email,
                password = currentState.password,
                telefono = currentState.telefono.ifBlank { null }
            )

            _state.update {
                when (resultado) {
                    is Resource.Success -> {
                        it.copy(
                            isLoading = false,
                            registroExitoso = true,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        it.copy(
                            isLoading = false,
                            error = resultado.message
                        )
                    }
                    is Resource.Loading -> {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun resetState() {
        _state.update { RegistroUiState() }
    }
}
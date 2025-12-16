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

    fun onEvent(event: RegistroUiEvent) {
        when (event) {
            is RegistroUiEvent.OnNombreChange -> onNombreChange(event.nombre)
            is RegistroUiEvent.OnEmailChange -> onEmailChange(event.email)
            is RegistroUiEvent.OnTelefonoChange -> onTelefonoChange(event.telefono)
            is RegistroUiEvent.OnPasswordChange -> onPasswordChange(event.password)
            is RegistroUiEvent.OnConfirmPasswordChange -> onConfirmPasswordChange(event.confirmPassword)
            RegistroUiEvent.TogglePasswordVisibility -> _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            RegistroUiEvent.Registrar -> registrar()
            RegistroUiEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun onNombreChange(nombre: String) {
        val filtered = nombre.filter { it.isLetter() || it == ' ' }.take(30)
        _state.update { it.copy(nombre = filtered) }
    }

    private fun onEmailChange(email: String) {
        val emailError = if (email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Email inválido"
        } else null
        _state.update { it.copy(email = email, emailError = emailError) }
    }

    private fun onTelefonoChange(telefono: String) {
        val filtered = telefono.filter { it.isDigit() }.take(10)
        _state.update { it.copy(telefono = filtered) }
    }

    private fun onPasswordChange(password: String) {
        val filtered = password.take(15)
        _state.update { it.copy(password = filtered) }
    }

    private fun onConfirmPasswordChange(confirmPassword: String) {
        val filtered = confirmPassword.take(15)
        _state.update { it.copy(confirmPassword = filtered) }
    }

    private fun esFormularioValido(): Boolean {
        val state = _state.value
        return state.nombre.isNotBlank() &&
                state.email.isNotBlank() &&
                state.emailError == null &&
                state.password.length >= 4 &&
                state.password == state.confirmPassword
    }

    private fun registrar() {
        if (!esFormularioValido()) {
            _state.update { it.copy(userMessage = "Por favor completa todos los campos correctamente") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = registrarUsuarioUseCase(
                nombre = _state.value.nombre,
                email = _state.value.email,
                password = _state.value.password,
                telefono = _state.value.telefono.ifBlank { null }
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            registroExitoso = true,
                            userMessage = "Registro exitoso"
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
}
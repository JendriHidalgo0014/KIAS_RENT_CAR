package edu.ucne.kias_rent_car.presentation.ProfileTareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private fun loadProfile() {
        viewModelScope.launch {
            val usuario = getUsuarioLogueadoUseCase()
            usuario?.let { u ->
                _state.update {
                    it.copy(
                        nombre = u.nombre,
                        email = u.email,
                        telefono = u.telefono ?: "No registrado",
                        fechaRegistro = "N/A",
                        direccion = "No registrada"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _state.update { it.copy(logoutSuccess = true) }
        }
    }
}
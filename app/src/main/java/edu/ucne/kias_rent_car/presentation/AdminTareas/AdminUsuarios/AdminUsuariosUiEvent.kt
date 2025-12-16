package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios

sealed interface AdminUsuariosUiEvent {
    data object NavigateBack : AdminUsuariosUiEvent
    data object NavigateToHome : AdminUsuariosUiEvent
    data object NavigateToReservas : AdminUsuariosUiEvent
    data object NavigateToVehiculos : AdminUsuariosUiEvent
    data object NavigateToProfile : AdminUsuariosUiEvent
    data class OnSearchChange(val query: String) : AdminUsuariosUiEvent
    data class DeleteUsuario(val id: String) : AdminUsuariosUiEvent
}
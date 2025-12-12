package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios

sealed interface AdminUsuariosUiEvent {
    data class OnSearchChange(val query: String) : AdminUsuariosUiEvent
    data class DeleteUsuario(val id: Int) : AdminUsuariosUiEvent
    object NavigateBack : AdminUsuariosUiEvent
    object NavigateToHome : AdminUsuariosUiEvent
    object NavigateToReservas : AdminUsuariosUiEvent
    object NavigateToVehiculos : AdminUsuariosUiEvent
    object NavigateToProfile : AdminUsuariosUiEvent
}
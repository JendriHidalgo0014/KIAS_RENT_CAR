package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome

sealed interface AdminHomeUiEvent {
    data object NavigateToVehiculos : AdminHomeUiEvent
    data object NavigateToReservas : AdminHomeUiEvent
    data object NavigateToUsuarios : AdminHomeUiEvent
    data object NavigateToMensajes : AdminHomeUiEvent
    data object NavigateToProfile : AdminHomeUiEvent
}
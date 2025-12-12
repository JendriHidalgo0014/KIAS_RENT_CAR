package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome

sealed interface AdminHomeUiEvent {
    object NavigateToVehiculos : AdminHomeUiEvent
    object NavigateToReservas : AdminHomeUiEvent
    object NavigateToUsuarios : AdminHomeUiEvent
    object NavigateToMensajes : AdminHomeUiEvent
    object NavigateToProfile : AdminHomeUiEvent
}
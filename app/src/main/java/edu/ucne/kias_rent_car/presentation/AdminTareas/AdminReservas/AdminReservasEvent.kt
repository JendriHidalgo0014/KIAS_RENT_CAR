package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

sealed interface AdminReservasEvent {
    data class FiltroChanged(val filtro: String) : AdminReservasEvent
    data object Refresh : AdminReservasEvent
    data class ReservaClicked(val reservacionId: Int) : AdminReservasEvent
    data object NavigateBack : AdminReservasEvent
    data object NavigateToHome : AdminReservasEvent
    data object NavigateToVehiculos : AdminReservasEvent
    data object NavigateToProfile : AdminReservasEvent
}
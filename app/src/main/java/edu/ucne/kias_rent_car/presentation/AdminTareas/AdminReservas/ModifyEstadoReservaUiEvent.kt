package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

sealed interface ModifyEstadoReservaUiEvent {
    data class LoadReservacion(val reservacionId: String) : ModifyEstadoReservaUiEvent
    data class OnEstadoChange(val estado: String) : ModifyEstadoReservaUiEvent
    data object GuardarCambios : ModifyEstadoReservaUiEvent
    data object NavigateBack : ModifyEstadoReservaUiEvent
}
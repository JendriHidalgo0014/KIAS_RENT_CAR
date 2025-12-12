package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

sealed interface ModifyEstadoReservaUiEvent {
    data class LoadReservacion(val reservacionId: Int) : ModifyEstadoReservaUiEvent
    data class OnEstadoChange(val estado: String) : ModifyEstadoReservaUiEvent
    object GuardarCambios : ModifyEstadoReservaUiEvent
    object NavigateBack : ModifyEstadoReservaUiEvent
}
package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas

data class ModifyEstadoReservaUiState(
    val reservacionId: String = "",
    val codigoReserva: String = "",
    val nombreCliente: String = "",
    val vehiculo: String = "",
    val periodo: String = "",
    val estadoSeleccionado: String = "",
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)
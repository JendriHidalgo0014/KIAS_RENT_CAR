package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

sealed interface AddVehiculoUiEvent {
    data class OnModeloChange(val modelo: String) : AddVehiculoUiEvent
    data class OnDescripcionChange(val descripcion: String) : AddVehiculoUiEvent
    data class OnCategoriaChange(val categoria: String) : AddVehiculoUiEvent
    data class OnTransmisionChange(val transmision: String) : AddVehiculoUiEvent
    data class OnAsientosChange(val asientos: Int) : AddVehiculoUiEvent
    data class OnPrecioChange(val precio: String) : AddVehiculoUiEvent
    data class OnFechaIngresoChange(val fecha: String) : AddVehiculoUiEvent
    data class OnImagenUrlChange(val url: String) : AddVehiculoUiEvent
    object GuardarVehiculo : AddVehiculoUiEvent
    object NavigateBack : AddVehiculoUiEvent
}
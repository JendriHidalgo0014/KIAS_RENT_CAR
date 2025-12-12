package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

sealed interface EditVehiculoUiEvent {
    data class LoadVehiculo(val id: String) : EditVehiculoUiEvent
    data class OnModeloChange(val modelo: String) : EditVehiculoUiEvent
    data class OnDescripcionChange(val descripcion: String) : EditVehiculoUiEvent
    data class OnCategoriaChange(val categoria: String) : EditVehiculoUiEvent
    data class OnTransmisionChange(val transmision: String) : EditVehiculoUiEvent
    data class OnAsientosChange(val asientos: Int) : EditVehiculoUiEvent
    data class OnPrecioChange(val precio: String) : EditVehiculoUiEvent
    data class OnImagenUrlChange(val url: String) : EditVehiculoUiEvent
    object GuardarCambios : EditVehiculoUiEvent
    object EliminarVehiculo : EditVehiculoUiEvent
    object NavigateBack : EditVehiculoUiEvent
}
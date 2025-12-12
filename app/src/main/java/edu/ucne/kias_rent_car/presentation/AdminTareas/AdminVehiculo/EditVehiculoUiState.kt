package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

data class EditVehiculoUiState(
    val vehiculoId: String = "",
    val modelo: String = "",
    val descripcion: String = "",
    val fechaIngreso: String = "",
    val precioPorDia: String = "",
    val imagenUrl: String = "",
    val categoria: String = "SUV",
    val asientos: Int = 5,
    val transmision: String = "Automatico",
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val deleteSuccess: Boolean = false,
    val error: String? = null,
    val isFormValid: Boolean = false
)
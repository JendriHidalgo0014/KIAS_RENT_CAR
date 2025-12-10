package edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo

data class AddVehiculoUiState(
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
    val error: String? = null
) {
    val isFormValid: Boolean
        get() = modelo.isNotBlank() &&
                descripcion.isNotBlank() &&
                precioPorDia.isNotBlank() &&
                (precioPorDia.toDoubleOrNull() ?: 0.0) > 0
}
package edu.ucne.kias_rent_car.presentation.ClienteTareas.UbicacionTareas

import edu.ucne.kias_rent_car.domain.model.Ubicacion
import java.time.LocalDate
import java.time.LocalTime

data class ReservationConfigUiState(
    val vehicleId: String = "",
    val ubicaciones: List<Ubicacion> = emptyList(),
    val lugarRecogida: Ubicacion? = null,
    val lugarDevolucion: Ubicacion? = null,
    val fechaRecogida: LocalDate? = null,
    val horaRecogida: LocalTime? = null,
    val fechaDevolucion: LocalDate? = null,
    val horaDevolucion: LocalTime? = null,
    val precioPorDia: Double = 0.0,
    val dias: Int = 0,
    val subtotal: Double = 0.0,
    val impuestos: Double = 0.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isFormValid: Boolean
        get() = lugarRecogida != null &&
                lugarDevolucion != null &&
                fechaRecogida != null &&
                horaRecogida != null &&
                fechaDevolucion != null &&
                horaDevolucion != null
}
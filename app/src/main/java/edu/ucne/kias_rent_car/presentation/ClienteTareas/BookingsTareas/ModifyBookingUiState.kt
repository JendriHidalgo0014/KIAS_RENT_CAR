package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import edu.ucne.kias_rent_car.domain.model.Ubicacion

data class ModifyBookingUiState(
    val bookingId: String = "",
    val ubicaciones: List<Ubicacion> = emptyList(),
    val ubicacionRecogida: Ubicacion? = null,
    val ubicacionDevolucion: Ubicacion? = null,
    val fechaRecogida: String = "",
    val fechaDevolucion: String = "",
    val fechaRecogidaDate: String = "",
    val horaRecogidaTime: String = "",
    val fechaDevolucionDate: String = "",
    val horaDevolucionTime: String = "",
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)
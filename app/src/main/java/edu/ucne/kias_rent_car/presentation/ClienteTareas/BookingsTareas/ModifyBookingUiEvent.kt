package edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas

import edu.ucne.kias_rent_car.domain.model.Ubicacion
import java.time.LocalDate
import java.time.LocalTime

sealed interface ModifyBookingUiEvent {
    data class LoadBooking(val bookingId: Int) : ModifyBookingUiEvent
    data class OnUbicacionRecogidaChange(val ubicacion: Ubicacion) : ModifyBookingUiEvent
    data class OnUbicacionDevolucionChange(val ubicacion: Ubicacion) : ModifyBookingUiEvent
    data class OnFechaRecogidaChange(val fecha: LocalDate, val hora: LocalTime) : ModifyBookingUiEvent
    data class OnFechaDevolucionChange(val fecha: LocalDate, val hora: LocalTime) : ModifyBookingUiEvent
    object GuardarCambios : ModifyBookingUiEvent
    object NavigateBack : ModifyBookingUiEvent
}
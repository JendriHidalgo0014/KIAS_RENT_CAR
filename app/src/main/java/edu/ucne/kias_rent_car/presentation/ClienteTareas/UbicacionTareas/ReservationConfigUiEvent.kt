package edu.ucne.kias_rent_car.presentation.ClienteTareas.UbicacionTareas

import edu.ucne.kias_rent_car.domain.model.Ubicacion
import java.time.LocalDate
import java.time.LocalTime

sealed interface ReservationConfigUiEvent {
    data class Init(val vehicleId: String) : ReservationConfigUiEvent
    data class OnLugarRecogidaChange(val ubicacion: Ubicacion) : ReservationConfigUiEvent
    data class OnLugarDevolucionChange(val ubicacion: Ubicacion) : ReservationConfigUiEvent
    data class OnFechaRecogidaChange(val fecha: LocalDate) : ReservationConfigUiEvent
    data class OnHoraRecogidaChange(val hora: LocalTime) : ReservationConfigUiEvent
    data class OnFechaDevolucionChange(val fecha: LocalDate) : ReservationConfigUiEvent
    data class OnHoraDevolucionChange(val hora: LocalTime) : ReservationConfigUiEvent
    object Continuar : ReservationConfigUiEvent
    object NavigateBack : ReservationConfigUiEvent
}
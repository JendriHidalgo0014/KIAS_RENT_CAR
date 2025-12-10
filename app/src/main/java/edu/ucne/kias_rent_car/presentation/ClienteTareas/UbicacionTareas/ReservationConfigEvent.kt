package edu.ucne.kias_rent_car.presentation.ClienteTareas.UbicacionTareas

import edu.ucne.kias_rent_car.domain.model.Ubicacion
import java.time.LocalDate
import java.time.LocalTime

sealed class ReservationConfigEvent {
    data class LugarRecogidaChanged(val ubicacion: Ubicacion) : ReservationConfigEvent()
    data class LugarDevolucionChanged(val ubicacion: Ubicacion) : ReservationConfigEvent()
    data class FechaRecogidaChanged(val fecha: LocalDate) : ReservationConfigEvent()
    data class HoraRecogidaChanged(val hora: LocalTime) : ReservationConfigEvent()
    data class FechaDevolucionChanged(val fecha: LocalDate) : ReservationConfigEvent()
    data class HoraDevolucionChanged(val hora: LocalTime) : ReservationConfigEvent()
    data object ClearError : ReservationConfigEvent()
}
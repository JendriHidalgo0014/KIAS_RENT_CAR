package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class UpdateEstadoReservacionUseCase @Inject constructor(
    private val repository: ReservacionRepository
) {
    suspend operator fun invoke(reservacionId: Int, nuevoEstado: String) {
        repository.updateEstado(reservacionId, nuevoEstado)
    }
}
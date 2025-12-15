package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class CancelReservacionUseCase @Inject constructor(
    private val repository: ReservacionRepository
) {
    suspend operator fun invoke(reservacionId: String): Resource<Unit> =
        repository.cancelReservacion(reservacionId)
}
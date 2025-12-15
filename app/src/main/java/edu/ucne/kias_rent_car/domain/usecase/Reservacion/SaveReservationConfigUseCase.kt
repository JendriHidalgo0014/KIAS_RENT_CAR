package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.ReservationConfig
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class SaveReservationConfigUseCase @Inject constructor(
    private val repository: ReservacionRepository
) {
    suspend operator fun invoke(config: ReservationConfig): Resource<Unit> =
        repository.saveReservationConfig(config)
}
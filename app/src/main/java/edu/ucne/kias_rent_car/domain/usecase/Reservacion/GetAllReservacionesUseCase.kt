package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class GetAllReservacionesUseCase @Inject constructor(
    private val repository: ReservacionRepository
) {
    suspend operator fun invoke(): Resource<List<Reservacion>> = repository.getReservaciones()
}
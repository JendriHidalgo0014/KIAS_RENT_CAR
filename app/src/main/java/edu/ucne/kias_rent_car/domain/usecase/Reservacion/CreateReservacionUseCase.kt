package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class CreateReservacionUseCase @Inject constructor(
    private val repository: ReservacionRepository
) {
    suspend operator fun invoke(): Resource<Reservacion> {
        return when (val configResult = repository.getReservationConfig()) {
            is Resource.Success -> {
                configResult.data?.let { repository.createReservacion(it) }
                    ?: Resource.Error("No hay configuración de reserva")
            }
            is Resource.Error -> Resource.Error(configResult.message)
            is Resource.Loading -> Resource.Loading
        }
    }
}
package edu.ucne.kias_rent_car.domain.usecase.Ubicacion

import edu.ucne.kias_rent_car.domain.model.Ubicacion
import edu.ucne.kias_rent_car.domain.repository.UbicacionRepository
import javax.inject.Inject

class GetUbicacionesUseCase @Inject constructor(
    private val ubicacionRepository: UbicacionRepository
) {
    suspend operator fun invoke(): List<Ubicacion> {
        return ubicacionRepository.getUbicaciones()
    }
}
package edu.ucne.kias_rent_car.domain.usecase.Mensaje

import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import javax.inject.Inject

class GetMensajeByIdUseCase @Inject constructor(
    private val repository: MensajeRepository
) {
    suspend operator fun invoke(id: Int): Mensaje? {
        return repository.getMensajeById(id)
    }
}
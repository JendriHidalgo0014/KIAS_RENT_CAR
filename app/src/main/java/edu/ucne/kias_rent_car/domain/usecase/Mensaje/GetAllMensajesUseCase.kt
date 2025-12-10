package edu.ucne.kias_rent_car.domain.usecase.Mensaje

import edu.ucne.kias_rent_car.domain.model.Mensaje
import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import javax.inject.Inject

class GetAllMensajesUseCase @Inject constructor(
    private val repository: MensajeRepository
) {
    suspend operator fun invoke(): List<Mensaje> {
        return repository.getMensajes()
    }
}
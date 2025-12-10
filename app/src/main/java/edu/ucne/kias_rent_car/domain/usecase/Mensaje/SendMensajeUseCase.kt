package edu.ucne.kias_rent_car.domain.usecase.Mensaje

import edu.ucne.kias_rent_car.domain.repository.MensajeRepository
import javax.inject.Inject

class SendMensajeUseCase @Inject constructor(
    private val repository: MensajeRepository
) {
    suspend operator fun invoke(asunto: String, mensaje: String) {
        repository.sendMensaje(asunto, mensaje)
    }
}
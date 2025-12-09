package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import javax.inject.Inject

class UpdateReservacionUseCase @Inject constructor(
    private val repository: ReservacionRepository
) {
    suspend operator fun invoke(
        reservacionId: Int,
        ubicacionRecogidaId: Int,
        ubicacionDevolucionId: Int,
        fechaRecogida: String,
        horaRecogida: String,
        fechaDevolucion: String,
        horaDevolucion: String
    ): Resource<Unit> {
        return repository.updateReservacionData(
            reservacionId = reservacionId,
            ubicacionRecogidaId = ubicacionRecogidaId,
            ubicacionDevolucionId = ubicacionDevolucionId,
            fechaRecogida = fechaRecogida,
            horaRecogida = horaRecogida,
            fechaDevolucion = fechaDevolucion,
            horaDevolucion = horaDevolucion
        )
    }
}
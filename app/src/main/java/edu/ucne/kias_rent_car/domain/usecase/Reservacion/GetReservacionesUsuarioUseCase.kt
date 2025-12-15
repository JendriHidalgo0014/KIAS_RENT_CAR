package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class GetReservacionesUsuarioUseCase @Inject constructor(
    private val reservacionRepository: ReservacionRepository,
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(): Resource<List<Reservacion>> {
        return when (val usuarioResult = usuarioRepository.getUsuarioLogueado()) {
            is Resource.Success -> {
                usuarioResult.data?.remoteId?.let {
                    reservacionRepository.getReservacionesByUsuario(it)
                } ?: Resource.Success(emptyList())
            }
            is Resource.Error -> Resource.Error(usuarioResult.message ?: "Error desconocido")
            is Resource.Loading -> Resource.Loading()
        }
    }
}
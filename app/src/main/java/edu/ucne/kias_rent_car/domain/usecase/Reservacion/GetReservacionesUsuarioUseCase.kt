package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import edu.ucne.kias_rent_car.domain.model.Reservacion
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class GetReservacionesUsuarioUseCase @Inject constructor(
    private val reservacionRepository: ReservacionRepository,
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(): List<Reservacion> {
        val usuario = usuarioRepository.getUsuarioLogueado()
        return if (usuario != null) {
            reservacionRepository.getReservacionesByUsuario(usuario.id)
        } else {
            emptyList()
        }
    }
}
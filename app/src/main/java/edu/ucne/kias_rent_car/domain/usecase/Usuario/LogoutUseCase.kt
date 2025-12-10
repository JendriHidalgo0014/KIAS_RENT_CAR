package edu.ucne.kias_rent_car.domain.usecase.Usuario

import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
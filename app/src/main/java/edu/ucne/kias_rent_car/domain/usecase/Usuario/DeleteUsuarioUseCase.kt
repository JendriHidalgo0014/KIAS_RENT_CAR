package edu.ucne.kias_rent_car.domain.usecase.Usuario

import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class DeleteUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return repository.deleteUsuario(id)
    }
}
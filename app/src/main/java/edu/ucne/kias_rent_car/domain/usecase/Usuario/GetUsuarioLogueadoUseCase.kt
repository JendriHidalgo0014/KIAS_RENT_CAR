package edu.ucne.kias_rent_car.domain.usecase.Usuario

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class GetUsuarioLogueadoUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(): Resource<Usuario?> = repository.getUsuarioLogueado()
}
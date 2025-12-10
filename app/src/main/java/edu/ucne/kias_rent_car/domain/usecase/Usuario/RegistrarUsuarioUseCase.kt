package edu.ucne.kias_rent_car.domain.usecase.Usuario

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Usuario
import edu.ucne.kias_rent_car.domain.repository.UsuarioRepository
import javax.inject.Inject

class RegistrarUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(
        nombre: String,
        email: String,
        password: String,
        telefono: String?
    ): Resource<Usuario> {
        return usuarioRepository.registrarUsuario(
            nombre = nombre.trim(),
            email = email.trim(),
            password = password,
            telefono = telefono?.trim()
        )
    }
}
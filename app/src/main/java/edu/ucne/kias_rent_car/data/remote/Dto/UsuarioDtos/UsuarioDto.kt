package edu.ucne.kias_rent_car.data.remote.dto

data class UsuarioDto(
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val password: String? = null,
    val telefono: String? = null,
    val rol: String? = null,
    val fechaRegistro: String? = null
)
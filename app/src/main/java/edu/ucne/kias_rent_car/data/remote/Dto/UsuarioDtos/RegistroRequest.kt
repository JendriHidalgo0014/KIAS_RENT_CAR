package edu.ucne.kias_rent_car.data.remote.Dto.UsuarioDtos

data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String?,
    val rol: String = "Cliente"

)
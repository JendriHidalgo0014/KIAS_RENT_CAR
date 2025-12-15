package edu.ucne.kias_rent_car.data.remote.dto

data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String? = null,
    val rol: String = "Cliente"
)
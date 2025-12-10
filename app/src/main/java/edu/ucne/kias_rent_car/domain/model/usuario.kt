package edu.ucne.kias_rent_car.domain.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val telefono: String?,
    val rol: String
) {
    fun esAdmin(): Boolean = rol == "Admin"
    fun esCliente(): Boolean = rol == "Cliente"
}
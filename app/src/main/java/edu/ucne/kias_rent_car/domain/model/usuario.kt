package edu.ucne.kias_rent_car.domain.model

import java.util.UUID

data class Usuario(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val email: String,
    val telefono: String? = null,
    val rol: String
) {
    fun esAdmin(): Boolean = rol == "Admin"
    fun esCliente(): Boolean = rol == "Cliente"
}
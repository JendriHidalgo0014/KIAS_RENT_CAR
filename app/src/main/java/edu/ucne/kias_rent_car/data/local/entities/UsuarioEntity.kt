package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String?,
    val rol: String,
    val fechaRegistro: String,
    val isLoggedIn: Boolean = false
)
package edu.ucne.kias_rent_car.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val email: String,
    val password: String = "",
    val telefono: String? = null,
    val rol: String,
    val isLoggedIn: Boolean = false
)
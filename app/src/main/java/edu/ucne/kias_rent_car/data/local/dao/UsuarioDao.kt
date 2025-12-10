package edu.ucne.kias_rent_car.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.ucne.kias_rent_car.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)
    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)
    @Query("SELECT * FROM usuarios")
    suspend fun getAllUsuarios(): List<UsuarioEntity>
    @Query("SELECT * FROM usuarios WHERE usuarioId = :id")
    suspend fun getUsuarioById(id: Int): UsuarioEntity?
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): UsuarioEntity?
    @Query("SELECT * FROM usuarios WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUsuario(): UsuarioEntity?
    @Query("SELECT * FROM usuarios WHERE isLoggedIn = 1 LIMIT 1")
    fun observeLoggedInUsuario(): Flow<UsuarioEntity?>
    @Query("UPDATE usuarios SET isLoggedIn = 0")
    suspend fun logoutAll()
    @Query("UPDATE usuarios SET isLoggedIn = 1 WHERE usuarioId = :id")
    suspend fun setLoggedIn(id: Int)
    @Query("DELETE FROM usuarios WHERE usuarioId = :id")
    suspend fun deleteUsuario(id: Int)
}
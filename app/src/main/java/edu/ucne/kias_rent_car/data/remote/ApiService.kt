package edu.ucne.kias_rent_car.data.remote

import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioRequest
import edu.ucne.kias_rent_car.data.remote.Dto.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET(".")
    suspend fun obtenerUsuarios(): Response<List<UsuarioResponse>>

    @GET("{id}")
    suspend fun obtenerUsuarioPorId(@Path("id") id: Int): Response<UsuarioResponse>

    @POST(".")
    suspend fun crearUsuario(@Body usuario: UsuarioRequest): Response<UsuarioResponse>

    @PUT("{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body usuario: UsuarioRequest
    ): Response<UsuarioResponse>

    @DELETE("{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Unit>
}
package edu.ucne.kias_rent_car.data.remote

import edu.ucne.kias_rent_car.data.remote.Dto.UbicacionDtos.UbicacionRequest
import edu.ucne.kias_rent_car.data.remote.dto.EstadoRequest
import edu.ucne.kias_rent_car.data.remote.dto.LoginRequest
import edu.ucne.kias_rent_car.data.remote.dto.MensajeDto
import edu.ucne.kias_rent_car.data.remote.dto.MensajeRequest
import edu.ucne.kias_rent_car.data.remote.dto.RegistroRequest
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionDto
import edu.ucne.kias_rent_car.data.remote.dto.ReservacionRequest
import edu.ucne.kias_rent_car.data.remote.dto.RespuestaRequest
import edu.ucne.kias_rent_car.data.remote.dto.UbicacionDto
import edu.ucne.kias_rent_car.data.remote.dto.UpdateDatosRequest
import edu.ucne.kias_rent_car.data.remote.dto.UsuarioDto
import edu.ucne.kias_rent_car.data.remote.dto.VehiculoDto
import edu.ucne.kias_rent_car.data.remote.dto.VehiculoRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // ==================== USUARIOS ====================
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioDto>

    @POST("usuarios/registro")
    suspend fun registro(@Body request: RegistroRequest): Response<UsuarioDto>

    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<UsuarioDto>>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<UsuarioDto>

    @GET("usuarios/email/{email}")
    suspend fun getUsuarioByEmail(@Path("email") email: String): Response<UsuarioDto>

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body request: RegistroRequest): Response<Unit>

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Unit>

    // ==================== VEHÍCULOS ====================
    @GET("vehiculos")
    suspend fun getVehiculosDisponibles(): Response<List<VehiculoDto>>

    @GET("vehiculos/all")
    suspend fun getAllVehiculos(): Response<List<VehiculoDto>>

    @GET("vehiculos/{id}")
    suspend fun getVehiculoById(@Path("id") id: Int): Response<VehiculoDto>

    @GET("vehiculos/categoria/{categoria}")
    suspend fun getVehiculosByCategoria(@Path("categoria") categoria: String): Response<List<VehiculoDto>>

    @GET("vehiculos/buscar")
    suspend fun searchVehiculos(@Query("query") query: String): Response<List<VehiculoDto>>

    @POST("vehiculos")
    suspend fun createVehiculo(@Body request: VehiculoRequest): Response<VehiculoDto>

    @PUT("vehiculos/{id}")
    suspend fun updateVehiculo(@Path("id") id: Int, @Body request: VehiculoRequest): Response<Unit>

    @DELETE("vehiculos/{id}")
    suspend fun deleteVehiculo(@Path("id") id: Int): Response<Unit>

    @Multipart
    @POST("vehiculos/upload-image")
    suspend fun uploadVehiculoImage(@Part file: MultipartBody.Part): Response<ImageUploadResponse>

    // ==================== UBICACIONES ====================
    @GET("ubicaciones")
    suspend fun getUbicaciones(): Response<List<UbicacionDto>>

    @GET("ubicaciones/{id}")
    suspend fun getUbicacionById(@Path("id") id: Int): Response<UbicacionDto>

    @POST("ubicaciones")
    suspend fun createUbicacion(@Body request: UbicacionRequest): Response<UbicacionDto>

    @PUT("ubicaciones/{id}")
    suspend fun updateUbicacion(@Path("id") id: Int, @Body request: UbicacionRequest): Response<Unit>

    @DELETE("ubicaciones/{id}")
    suspend fun deleteUbicacion(@Path("id") id: Int): Response<Unit>

    // ==================== RESERVACIONES ====================
    @GET("reservaciones")
    suspend fun getReservaciones(): Response<List<ReservacionDto>>

    @GET("reservaciones/{id}")
    suspend fun getReservacionById(@Path("id") id: Int): Response<ReservacionDto>

    @GET("reservaciones/usuario/{usuarioId}")
    suspend fun getReservacionesByUsuario(@Path("usuarioId") usuarioId: Int): Response<List<ReservacionDto>>

    @GET("reservaciones/codigo/{codigo}")
    suspend fun getReservacionByCodigo(@Path("codigo") codigo: String): Response<ReservacionDto>

    @POST("reservaciones")
    suspend fun createReservacion(@Body request: ReservacionRequest): Response<ReservacionDto>

    @PUT("reservaciones/{id}")
    suspend fun updateReservacion(@Path("id") id: Int, @Body request: Map<String, Any>): Response<Unit>

    @PUT("reservaciones/{id}/datos")
    suspend fun updateReservacionDatos(@Path("id") id: Int, @Body request: UpdateDatosRequest): Response<Unit>

    @PUT("reservaciones/{id}/estado")
    suspend fun updateEstadoReservacion(@Path("id") id: Int, @Body request: EstadoRequest): Response<Unit>

    @DELETE("reservaciones/{id}")
    suspend fun deleteReservacion(@Path("id") id: Int): Response<Unit>

    // ==================== MENSAJES ====================
    @GET("mensajes")
    suspend fun getMensajes(): Response<List<MensajeDto>>

    @GET("mensajes/{id}")
    suspend fun getMensajeById(@Path("id") id: Int): Response<MensajeDto>

    @GET("mensajes/usuario/{usuarioId}")
    suspend fun getMensajesByUsuario(@Path("usuarioId") usuarioId: Int): Response<List<MensajeDto>>

    @POST("mensajes")
    suspend fun createMensaje(@Body request: MensajeRequest): Response<MensajeDto>

    @PUT("mensajes/{id}/responder")
    suspend fun responderMensaje(@Path("id") id: Int, @Body request: RespuestaRequest): Response<Unit>

    @PUT("mensajes/{id}/leido")
    suspend fun marcarMensajeLeido(@Path("id") id: Int): Response<Unit>

    @DELETE("mensajes/{id}")
    suspend fun deleteMensaje(@Path("id") id: Int): Response<Unit>
}

data class ImageUploadResponse(
    val url: String
)
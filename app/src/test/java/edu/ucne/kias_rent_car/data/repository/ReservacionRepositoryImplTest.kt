package edu.ucne.kias_rent_car.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.kias_rent_car.data.local.dao.ReservacionDao
import edu.ucne.kias_rent_car.data.local.dao.ReservationConfigDao
import edu.ucne.kias_rent_car.data.local.dao.UbicacionDao
import edu.ucne.kias_rent_car.data.local.dao.UsuarioDao
import edu.ucne.kias_rent_car.data.local.dao.VehicleDao
import edu.ucne.kias_rent_car.data.local.entity.ReservacionEntity
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.remote.datasource.ReservacionRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReservacionRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: ReservacionRepositoryImpl
    private lateinit var context: Context
    private lateinit var remoteDataSource: ReservacionRemoteDataSource
    private lateinit var reservacionDao: ReservacionDao
    private lateinit var reservationConfigDao: ReservationConfigDao
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var vehicleDao: VehicleDao
    private lateinit var ubicacionDao: UbicacionDao

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        reservacionDao = mockk(relaxed = true)
        reservationConfigDao = mockk(relaxed = true)
        usuarioDao = mockk(relaxed = true)
        vehicleDao = mockk(relaxed = true)
        ubicacionDao = mockk(relaxed = true)

        repository = ReservacionRepositoryImpl(
            context,
            remoteDataSource,
            reservacionDao,
            reservationConfigDao,
            usuarioDao,
            vehicleDao,
            ubicacionDao
        )
    }

    @Test
    fun `getReservaciones retorna datos locales cuando remoto falla`() = runTest {
        val localEntities = listOf(
            ReservacionEntity(
                reservacionId = 1,
                usuarioId = 1,
                vehiculoId = 1,
                fechaRecogida = "2025-01-15",
                horaRecogida = "10:00",
                fechaDevolucion = "2025-01-20",
                horaDevolucion = "10:00",
                ubicacionRecogidaId = 1,
                ubicacionDevolucionId = 1,
                estado = "Confirmada",
                subtotal = 500.0,
                impuestos = 90.0,
                total = 590.0,
                codigoReserva = "KR-123456",
                fechaCreacion = "2025-01-10",
                vehiculoModelo = "Kia K5",
                vehiculoImagenUrl = "",
                vehiculoPrecioPorDia = 100.0,
                ubicacionRecogidaNombre = "Aeropuerto",
                ubicacionDevolucionNombre = "Aeropuerto"
            )
        )
        coEvery { remoteDataSource.getReservaciones() } throws Exception("Network error")
        coEvery { reservacionDao.getReservaciones() } returns localEntities

        val result = repository.getReservaciones()

        assertEquals(1, result.size)
        assertEquals("KR-123456", result[0].codigoReserva)
        coVerify { reservacionDao.getReservaciones() }
    }

    @Test
    fun `updateEstado actualiza localmente primero`() = runTest {
        val reservacionId = 1
        val nuevoEstado = "Cancelada"
        val entity = ReservacionEntity(
            reservacionId = 1,
            usuarioId = 1,
            vehiculoId = 5,
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            ubicacionRecogidaId = 1,
            ubicacionDevolucionId = 1,
            estado = "Confirmada",
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            codigoReserva = "KR-123456",
            fechaCreacion = "2025-01-10",
            vehiculoModelo = "Kia K5",
            vehiculoImagenUrl = "",
            vehiculoPrecioPorDia = 100.0,
            ubicacionRecogidaNombre = "Aeropuerto",
            ubicacionDevolucionNombre = "Aeropuerto"
        )
        coEvery { reservacionDao.getById(reservacionId) } returns entity
        coEvery { reservacionDao.updateEstadoLocal(reservacionId, nuevoEstado) } just Runs
        coEvery { vehicleDao.updateDisponibilidad(any(), any()) } just Runs
        coEvery { remoteDataSource.updateEstado(reservacionId, nuevoEstado) } returns true
        coEvery { reservacionDao.markEstadoAsUpdated(reservacionId) } just Runs

        val result = repository.updateEstado(reservacionId, nuevoEstado)

        assertTrue(result is Resource.Success)
        coVerify { reservacionDao.updateEstadoLocal(reservacionId, nuevoEstado) }
    }

    @Test
    fun `updateEstado libera vehiculo cuando estado es Cancelada`() = runTest {
        val reservacionId = 1
        val nuevoEstado = "Cancelada"
        val entity = ReservacionEntity(
            reservacionId = 1,
            usuarioId = 1,
            vehiculoId = 5,
            fechaRecogida = "2025-01-15",
            horaRecogida = "10:00",
            fechaDevolucion = "2025-01-20",
            horaDevolucion = "10:00",
            ubicacionRecogidaId = 1,
            ubicacionDevolucionId = 1,
            estado = "Confirmada",
            subtotal = 500.0,
            impuestos = 90.0,
            total = 590.0,
            codigoReserva = "KR-123456",
            fechaCreacion = "2025-01-10",
            vehiculoModelo = "Kia K5",
            vehiculoImagenUrl = "",
            vehiculoPrecioPorDia = 100.0,
            ubicacionRecogidaNombre = "Aeropuerto",
            ubicacionDevolucionNombre = "Aeropuerto"
        )
        coEvery { reservacionDao.getById(reservacionId) } returns entity
        coEvery { reservacionDao.updateEstadoLocal(any(), any()) } just Runs
        coEvery { vehicleDao.updateDisponibilidad("5", true) } just Runs
        coEvery { remoteDataSource.updateEstado(any(), any()) } returns true
        coEvery { reservacionDao.markEstadoAsUpdated(any()) } just Runs

        repository.updateEstado(reservacionId, nuevoEstado)

        coVerify { vehicleDao.updateDisponibilidad("5", true) }
    }
}
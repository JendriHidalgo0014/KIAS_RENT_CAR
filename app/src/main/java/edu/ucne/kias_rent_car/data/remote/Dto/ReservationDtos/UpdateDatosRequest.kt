package edu.ucne.kias_rent_car.data.remote.Dto.ReservationDtos

data class UpdateDatosRequest(
    val ubicacionRecogidaId: Int,
    val ubicacionDevolucionId: Int,
    val fechaRecogida: String,
    val horaRecogida: String,
    val fechaDevolucion: String,
    val horaDevolucion: String
)
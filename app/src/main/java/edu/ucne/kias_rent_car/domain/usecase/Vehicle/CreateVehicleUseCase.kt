package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.repository.VehicleRepositoryImpl
import javax.inject.Inject

class CreateVehicleUseCase @Inject constructor(
    private val repository: VehicleRepositoryImpl
) {
    suspend operator fun invoke(
        modelo: String,
        descripcion: String,
        precioPorDia: Double,
        imagenUrl: String
    ) {
        repository.createVehicle(modelo, descripcion, precioPorDia, imagenUrl)
    }
}
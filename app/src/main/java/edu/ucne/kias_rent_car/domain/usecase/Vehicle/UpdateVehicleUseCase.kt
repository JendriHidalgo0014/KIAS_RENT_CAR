package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.repository.VehicleRepositoryImpl
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(
    private val repository: VehicleRepositoryImpl
) {
    suspend operator fun invoke(
        id: String,
        modelo: String,
        descripcion: String,
        precioPorDia: Double,
        imagenUrl: String
    ) {
        repository.updateVehicle(id, modelo, descripcion, precioPorDia, imagenUrl)
    }
}
package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(
        id: String,
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Unit> = repository.updateVehicle(
        id, modelo, descripcion, categoria, asientos, transmision, precioPorDia, imagenUrl
    )
}
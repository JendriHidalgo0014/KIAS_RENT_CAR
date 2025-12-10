package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.repository.VehicleRepositoryImpl
import javax.inject.Inject

class DeleteVehicleUseCase @Inject constructor(
    private val repository: VehicleRepositoryImpl
) {
    suspend operator fun invoke(id: String) {
        repository.deleteVehicle(id)
    }
}
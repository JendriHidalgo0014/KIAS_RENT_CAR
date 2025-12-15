package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import javax.inject.Inject

class GetVehicleDetailUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(vehicleId: String): Resource<Vehicle> = repository.getVehicle(vehicleId)
}
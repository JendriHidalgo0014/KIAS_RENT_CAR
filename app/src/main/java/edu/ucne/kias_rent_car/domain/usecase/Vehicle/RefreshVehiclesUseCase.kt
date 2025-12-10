package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import javax.inject.Inject

class RefreshVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.refreshVehicles()
    }
}
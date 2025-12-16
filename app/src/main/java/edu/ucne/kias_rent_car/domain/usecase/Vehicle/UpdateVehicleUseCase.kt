package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.model.VehicleParams
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(params: VehicleParams): Resource<Unit> =
        repository.updateVehicle(params)
}
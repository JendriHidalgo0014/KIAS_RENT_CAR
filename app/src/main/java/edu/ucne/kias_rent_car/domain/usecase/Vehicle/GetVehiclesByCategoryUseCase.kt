package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.model.VehicleCategory
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVehiclesByCategoryUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(category: VehicleCategory): Flow<List<Vehicle>> {
        return repository.observeVehiclesByCategory(category)
    }
}
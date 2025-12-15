package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.domain.model.Vehicle
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    operator fun invoke(query: String): Flow<List<Vehicle>> = repository.searchVehicles(query)
}
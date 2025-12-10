package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.repository.VehicleRepositoryImpl
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(
    private val repository: VehicleRepositoryImpl
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
    ): Resource<Unit> {
        return repository.updateVehicle(
            id = id,
            modelo = modelo,
            descripcion = descripcion,
            categoria = categoria,
            asientos = asientos,
            transmision = transmision,
            precioPorDia = precioPorDia,
            imagenUrl = imagenUrl
        )
    }
}
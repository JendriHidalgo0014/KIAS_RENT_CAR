package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.data.repository.VehicleRepositoryImpl
import edu.ucne.kias_rent_car.domain.model.Vehicle
import javax.inject.Inject

class CreateVehicleUseCase @Inject constructor(
    private val repository: VehicleRepositoryImpl
) {
    suspend operator fun invoke(
        modelo: String,
        descripcion: String,
        categoria: String,
        asientos: Int,
        transmision: String,
        precioPorDia: Double,
        imagenUrl: String
    ): Resource<Vehicle> {
        return repository.createVehicle(
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
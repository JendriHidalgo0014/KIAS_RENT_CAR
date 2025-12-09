package edu.ucne.kias_rent_car.domain.model

data class Vehicle(
    val id: String,
    val remoteId: Int,
    val modelo: String,
    val descripcion: String,
    val categoria: VehicleCategory,
    val asientos: Int,
    val transmision: TransmisionType,
    val precioPorDia: Double,
    val imagenUrl: String,
    val disponible: Boolean
)
enum class VehicleCategory(val displayName: String) {
    ALL("Todos"),
    SUV("SUV"),
    SEDAN("Sedán"),
    HATCHBACK("Hatchback"),
    ELECTRIC("Eléctrico"),
    HYBRID("Híbrido")
}
enum class TransmisionType(val displayName: String) {
    AUTOMATIC("Automático"),
    MANUAL("Manual")
}
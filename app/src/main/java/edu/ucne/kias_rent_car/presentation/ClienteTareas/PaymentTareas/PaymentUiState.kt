package edu.ucne.kias_rent_car.presentation.PaymentTareas

data class PaymentUiState(
    val vehicleId: String = "",
    val total: Double = 0.0,
    val metodoPago: MetodoPago = MetodoPago.TARJETA,
    val numeroTarjeta: String = "",
    val vencimiento: String = "",
    val cvv: String = "",
    val nombreTitular: String = "",
    val guardarTarjeta: Boolean = false,
    val isLoading: Boolean = false,
    val paymentSuccess: Boolean = false,
    val reservacionId: String? = null,
    val isFormValid: Boolean = false
)

enum class MetodoPago {
    TARJETA,
    BILLETERA
}
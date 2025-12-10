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
    val error: String? = null,
    val paymentSuccess: Boolean = false,
    val reservacionId: String? = null
) {
    val isFormValid: Boolean
        get() = when (metodoPago) {
            MetodoPago.TARJETA -> numeroTarjeta.length >= 16 &&
                    vencimiento.length >= 4 &&
                    cvv.length >= 3 &&
                    nombreTitular.isNotBlank()
            MetodoPago.BILLETERA -> true
        }
}

enum class MetodoPago {
    TARJETA,
    BILLETERA
}
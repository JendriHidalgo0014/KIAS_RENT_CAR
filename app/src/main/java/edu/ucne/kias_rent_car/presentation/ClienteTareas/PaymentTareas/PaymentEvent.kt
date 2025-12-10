package edu.ucne.kias_rent_car.presentation.PaymentTareas

sealed class PaymentEvent {
    data class MetodoPagoChanged(val metodo: MetodoPago) : PaymentEvent()
    data class NumeroTarjetaChanged(val numero: String) : PaymentEvent()
    data class VencimientoChanged(val vencimiento: String) : PaymentEvent()
    data class CvvChanged(val cvv: String) : PaymentEvent()
    data class NombreTitularChanged(val nombre: String) : PaymentEvent()
    data class GuardarTarjetaChanged(val guardar: Boolean) : PaymentEvent()
    data object ProcesarPago : PaymentEvent()
}
package edu.ucne.kias_rent_car.presentation.PaymentTareas

sealed interface PaymentUiEvent {
    data class Init(val vehicleId: String) : PaymentUiEvent
    data class OnMetodoPagoChange(val metodo: MetodoPago) : PaymentUiEvent
    data class OnNumeroTarjetaChange(val numero: String) : PaymentUiEvent
    data class OnVencimientoChange(val vencimiento: String) : PaymentUiEvent
    data class OnCvvChange(val cvv: String) : PaymentUiEvent
    data class OnNombreTitularChange(val nombre: String) : PaymentUiEvent
    data class OnGuardarTarjetaChange(val guardar: Boolean) : PaymentUiEvent
    object ProcesarPago : PaymentUiEvent
    object NavigateBack : PaymentUiEvent
}
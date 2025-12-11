package edu.ucne.kias_rent_car.domain.model

object EstadoReserva {
    const val TODOS = "Todos"
    const val RESERVADO = "Reservado"
    const val EN_PROCESO = "En Proceso"
    const val EN_PROCESO_SIN_ESPACIO = "EnProceso"
    const val FINALIZADO = "Finalizado"
    const val FINALIZADA = "Finalizada"
    const val CONFIRMADA = "Confirmada"
    const val CANCELADA = "Cancelada"
    const val PENDIENTE = "Pendiente"

    val FILTROS_ADMIN = listOf(TODOS, RESERVADO, EN_PROCESO, FINALIZADO)
    val OPCIONES_MODIFICAR = listOf(CONFIRMADA, CANCELADA, EN_PROCESO, FINALIZADA)
    val ESTADOS_ACTUALES = listOf(CONFIRMADA, PENDIENTE, EN_PROCESO_SIN_ESPACIO, EN_PROCESO, RESERVADO)
    val ESTADOS_PASADOS = listOf(FINALIZADA, CANCELADA)
}
package edu.ucne.kias_rent_car.navigation

sealed class Screen(val route: String) {

    // Auth
    object Login : Screen("login")
    object Register : Screen("register")

    // Cliente
    object Home : Screen("home")
    object VehicleDetail : Screen("vehicle_detail/{vehicleId}") {
        fun createRoute(vehicleId: String) = "vehicle_detail/$vehicleId"
    }
    object ReservationConfig : Screen("reservation_config/{vehicleId}") {
        fun createRoute(vehicleId: String) = "reservation_config/$vehicleId"
    }
    object ReservationConfirmation : Screen("reservation_confirmation/{vehicleId}") {
        fun createRoute(vehicleId: String) = "reservation_confirmation/$vehicleId"
    }
    object Payment : Screen("payment/{vehicleId}") {
        fun createRoute(vehicleId: String) = "payment/$vehicleId"
    }
    object ReservationSuccess : Screen("reservation_success/{reservacionId}") {
        fun createRoute(reservacionId: String) = "reservation_success/$reservacionId"
    }
    object Bookings : Screen("bookings")
    object BookingDetail : Screen("booking_detail/{bookingId}") {
        fun createRoute(bookingId: Int) = "booking_detail/$bookingId"
    }
    object ModifyBooking : Screen("modify_booking/{bookingId}") {
        fun createRoute(bookingId: Int) = "modify_booking/$bookingId"
    }
    object Support : Screen("support")
    object SendMessage : Screen("send_message")
    object Profile : Screen("profile")

    // Admin
    object AdminHome : Screen("admin_home")
    object AdminVehiculos : Screen("admin_vehiculos")
    object AddVehiculo : Screen("add_vehiculo")
    object EditVehiculo : Screen("edit_vehiculo/{vehiculoId}") {
        fun createRoute(vehiculoId: String) = "edit_vehiculo/$vehiculoId"
    }
    object AdminReservas : Screen("admin_reservas")
    object ModifyEstadoReserva : Screen("modify_estado_reserva/{reservacionId}") {
        fun createRoute(reservacionId: Int) = "modify_estado_reserva/$reservacionId"
    }
    object AdminUsuarios : Screen("admin_usuarios")
    object AdminMensajes : Screen("admin_mensajes")
    object ResponderMensaje : Screen("responder_mensaje/{mensajeId}") {
        fun createRoute(mensajeId: Int) = "responder_mensaje/$mensajeId"
    }
    object AdminProfile : Screen("admin_profile")
}
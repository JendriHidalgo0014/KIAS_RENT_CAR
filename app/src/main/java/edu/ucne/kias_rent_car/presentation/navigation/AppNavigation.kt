package edu.ucne.kias_rent_car.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminHome.AdminHomeScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje.AdminMensajesScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminMensaje.ResponderMensajeScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminProfileScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas.AdminReservasScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminReservas.ModifyEstadoReservaScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminUsuarios.AdminUsuariosScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo.AddVehiculoScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo.AdminVehiculosScreen
import edu.ucne.kias_rent_car.presentation.AdminTareas.AdminVehiculo.EditVehiculoScreen
import edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas.BookingDetailScreen
import edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas.BookingsScreen
import edu.ucne.kias_rent_car.presentation.ClienteTareas.BookingsTareas.ModifyBookingScreen
import edu.ucne.kias_rent_car.presentation.ClienteTareas.UbicacionTareas.ReservationConfigScreen
import edu.ucne.kias_rent_car.presentation.ClienteTareas.VehiculoDetalleTareas.VehicleDetailScreen
import edu.ucne.kias_rent_car.presentation.HomeClienteTareas.HomeScreen
import edu.ucne.kias_rent_car.presentation.LoginTareas.LoginScreen
import edu.ucne.kias_rent_car.presentation.LoginTareas.RegistroScreen
import edu.ucne.kias_rent_car.presentation.PaymentTareas.PaymentScreen
import edu.ucne.kias_rent_car.presentation.ProfileTareas.ProfileScreen
import edu.ucne.kias_rent_car.presentation.ReservationTareas.ReservationConfirmationScreen
import edu.ucne.kias_rent_car.presentation.ReservationTareas.ReservationSuccessScreen
import edu.ucne.kias_rent_car.presentation.SupportTareas.SendMessageScreen
import edu.ucne.kias_rent_car.presentation.SupportTareas.SupportScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ==================== AUTH ====================
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginExitoso = { isAdmin: Boolean ->
                    if (isAdmin) {
                        navController.navigate(Screen.AdminHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegistro = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== CLIENTE ====================
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToVehicleDetail = { vehicleId: String ->
                    navController.navigate(Screen.VehicleDetail.createRoute(vehicleId))
                },
                onNavigateToBookings = {
                    navController.navigate(Screen.Bookings.route)
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.VehicleDetail.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            VehicleDetailScreen(
                vehicleId = vehicleId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReservation = {
                    navController.navigate(Screen.ReservationConfig.createRoute(vehicleId))
                }
            )
        }

        composable(
            route = Screen.ReservationConfig.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            ReservationConfigScreen(
                vehicleId = vehicleId,
                onNavigateBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(Screen.ReservationConfirmation.createRoute(vehicleId))
                }
            )
        }

        composable(
            route = Screen.ReservationConfirmation.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            ReservationConfirmationScreen(
                vehicleId = vehicleId,
                onNavigateBack = { navController.popBackStack() },
                onConfirm = {
                    navController.navigate(Screen.Payment.createRoute(vehicleId))
                },
                onCancel = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            PaymentScreen(
                vehicleId = vehicleId,
                onNavigateBack = { navController.popBackStack() },
                onPaymentSuccess = { reservacionId: String ->
                    navController.navigate(Screen.ReservationSuccess.createRoute(reservacionId)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Screen.ReservationSuccess.route,
            arguments = listOf(navArgument("reservacionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reservacionId = backStackEntry.arguments?.getString("reservacionId") ?: ""
            ReservationSuccessScreen(
                reservacionId = reservacionId,
                onVerDetalles = { id: String ->
                    navController.navigate(Screen.BookingDetail.createRoute(id.toIntOrNull() ?: 0)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onVolverInicio = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Bookings.route) {
            BookingsScreen(
                onNavigateToBookingDetail = { bookingId: Int ->
                    navController.navigate(Screen.BookingDetail.createRoute(bookingId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.BookingDetail.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
            BookingDetailScreen(
                bookingId = bookingId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToModify = { id: Int ->
                    navController.navigate(Screen.ModifyBooking.createRoute(id))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToBookings = {
                    navController.navigate(Screen.Bookings.route)
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.ModifyBooking.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
            ModifyBookingScreen(
                bookingId = bookingId,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.Support.route) {
            SupportScreen(
                onNavigateToSendMessage = {
                    navController.navigate(Screen.SendMessage.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToBookings = {
                    navController.navigate(Screen.Bookings.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.SendMessage.route) {
            SendMessageScreen(
                onNavigateBack = { navController.popBackStack() },
                onMessageSent = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToBookings = {
                    navController.navigate(Screen.Bookings.route)
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                }
            )
        }

        // ==================== ADMIN ====================
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                onNavigateToVehiculos = {
                    navController.navigate(Screen.AdminVehiculos.route)
                },
                onNavigateToReservas = {
                    navController.navigate(Screen.AdminReservas.route)
                },
                onNavigateToUsuarios = {
                    navController.navigate(Screen.AdminUsuarios.route)
                },
                onNavigateToMensajes = {
                    navController.navigate(Screen.AdminMensajes.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.AdminProfile.route)
                }
            )
        }

        composable(Screen.AdminVehiculos.route) {
            AdminVehiculosScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddVehiculo = {
                    navController.navigate(Screen.AddVehiculo.route)
                },
                onNavigateToEditVehiculo = { vehiculoId: String ->
                    navController.navigate(Screen.EditVehiculo.createRoute(vehiculoId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                },
                onNavigateToReservas = {
                    navController.navigate(Screen.AdminReservas.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.AdminProfile.route)
                }
            )
        }

        composable(Screen.AddVehiculo.route) {
            AddVehiculoScreen(
                onNavigateBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditVehiculo.route,
            arguments = listOf(navArgument("vehiculoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId") ?: ""
            EditVehiculoScreen(
                vehiculoId = vehiculoId,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() },
                onDelete = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminReservas.route) {
            AdminReservasScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToModifyEstado = { reservacionId: Int ->
                    navController.navigate(Screen.ModifyEstadoReserva.createRoute(reservacionId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                },
                onNavigateToVehiculos = {
                    navController.navigate(Screen.AdminVehiculos.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.AdminProfile.route)
                }
            )
        }

        composable(
            route = Screen.ModifyEstadoReserva.route,
            arguments = listOf(navArgument("reservacionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val reservacionId = backStackEntry.arguments?.getInt("reservacionId") ?: 0
            ModifyEstadoReservaScreen(
                reservacionId = reservacionId,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminUsuarios.route) {
            AdminUsuariosScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                },
                onNavigateToReservas = {
                    navController.navigate(Screen.AdminReservas.route)
                },
                onNavigateToVehiculos = {
                    navController.navigate(Screen.AdminVehiculos.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.AdminProfile.route)
                }
            )
        }

        composable(Screen.AdminMensajes.route) {
            AdminMensajesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResponder = { mensajeId: Int ->
                    navController.navigate(Screen.ResponderMensaje.createRoute(mensajeId))
                },
                onNavigateToHome = {
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                },
                onNavigateToReservas = {
                    navController.navigate(Screen.AdminReservas.route)
                },
                onNavigateToVehiculos = {
                    navController.navigate(Screen.AdminVehiculos.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.AdminProfile.route)
                }
            )
        }

        composable(
            route = Screen.ResponderMensaje.route,
            arguments = listOf(navArgument("mensajeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val mensajeId = backStackEntry.arguments?.getInt("mensajeId") ?: 0
            ResponderMensajeScreen(
                mensajeId = mensajeId,
                onNavigateBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminProfile.route) {
            AdminProfileScreen(
                onNavigateToAdminHome = {
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
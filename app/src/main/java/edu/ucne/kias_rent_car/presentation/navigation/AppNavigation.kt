package edu.ucne.kias_rent_car.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.kias_rent_car.presentation.navigation.LoginRoute
import edu.ucne.kias_rent_car.presentation.navigation.RegistroRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute  // ← Inicia en Login
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginExitoso = {
                    // Aquí puedes navegar a tu pantalla principal cuando la tengas
                    // Por ahora, solo cerramos sesión del login
                },
                onNavigateToRegistro = {
                    navController.navigate(RegistroRoute)
                }
            )
        }

        composable<RegistroRoute> {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.popBackStack()  // Vuelve al login después de registrarse
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
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
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginExitoso = {

                },
                onNavigateToRegistro = {
                    navController.navigate(RegistroRoute)
                }
            )
        }

        composable<RegistroRoute> {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
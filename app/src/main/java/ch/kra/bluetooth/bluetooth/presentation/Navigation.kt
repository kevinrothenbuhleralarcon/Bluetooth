package ch.kra.bluetooth.bluetooth.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ch.kra.bluetooth.bluetooth.presentation.device_control.screen.DeviceControlScreen
import ch.kra.bluetooth.core.Routes
import ch.kra.bluetooth.bluetooth.presentation.device_selection.screen.DeviceSelectionScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DeviceSelection.route
    ) {
        composable(Routes.DeviceSelection.route) {
            DeviceSelectionScreen(
                navigate = { event ->
                    navController.navigate(event.route)
                }
            )
        }

        composable(
            route = Routes.DeviceControl.route + "?deviceAddress={deviceAddress}",
            arguments = listOf(
                navArgument(name = "deviceAddress") {
                    type = NavType.StringType
                }
            )
        ) {
            DeviceControlScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
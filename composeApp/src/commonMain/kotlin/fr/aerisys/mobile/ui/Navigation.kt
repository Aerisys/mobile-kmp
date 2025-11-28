package fr.aerisys.mobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.aerisys.mobile.ui.screens.AccountScreen
import fr.aerisys.mobile.ui.screens.AddCameraScreen
import fr.aerisys.mobile.ui.screens.CameraDetailsScreen
import fr.aerisys.mobile.ui.screens.CameraListScreen
import fr.aerisys.mobile.ui.screens.CameraStreamScreen
import fr.aerisys.mobile.ui.screens.HomeScreen
import fr.aerisys.mobile.viewModel.CameraViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

class Routes {
    @Serializable
    data object HomeRoute

    @Serializable
    data object CameraListRoute

    @Serializable
    data class CameraStreamRoute(val id: Long?)

    @Serializable
    data class CameraDetailsRoute(val id: Long?)

    @Serializable
    data object AccountRoute

    @Serializable
    data object AddCameraRoute
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navHostController = rememberNavController()
    val cameraViewModel = koinViewModel<CameraViewModel>()

    NavHost(
        navController = navHostController,
        startDestination = Routes.AccountRoute,
        modifier = modifier
    ) {
        composable<Routes.HomeRoute> {
            HomeScreen(
                onNavigateToCameraList = {
                    navHostController.navigate(Routes.CameraListRoute)
                }
            )
        }
        composable<Routes.AccountRoute> {
            AccountScreen(navController = navHostController)
        }
        composable<Routes.CameraDetailsRoute> {
            val cameraRoute = it.toRoute<Routes.CameraDetailsRoute>()
            if (cameraRoute.id == null) {
                return@composable
            }
            val cameraBean = cameraViewModel.camerasList.collectAsStateWithLifecycle()
                .value.first { w -> w.id == cameraRoute.id }
            CameraDetailsScreen(
                cameraBean = cameraBean,
                navController = navHostController,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
        composable<Routes.AddCameraRoute> {
            AddCameraScreen(
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
        composable<Routes.CameraStreamRoute> {
            val cameraRoute = it.toRoute<Routes.CameraStreamRoute>()
            if (cameraRoute.id == null) {
                return@composable
            }
            val cameraBean = cameraViewModel.camerasList.collectAsStateWithLifecycle()
                .value.first { w -> w.id == cameraRoute.id }

            CameraStreamScreen(
                cameraBean = cameraBean,
                navHostController = navHostController
            )
        }
        composable<Routes.CameraListRoute> {
            CameraListScreen(
                navController = navHostController,
            )
        }
    }
}
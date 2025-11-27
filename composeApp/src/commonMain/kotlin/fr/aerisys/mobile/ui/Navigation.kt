package fr.aerisys.mobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.aerisys.mobile.ui.screens.AccountScreen
import fr.aerisys.mobile.ui.screens.CameraDetailsScreen
import fr.aerisys.mobile.ui.screens.CameraListScreen
import fr.aerisys.mobile.ui.screens.CameraEndScreen
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
    data object CameraEndRoute

    @Serializable
    data class CameraStreamRoute(val id: Long)

    @Serializable
    data class CameraDetailsRoute(val id: Long)

    @Serializable
    data object AccountRoute
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navHostController = rememberNavController()
    val cameraViewModel = koinViewModel<CameraViewModel>()
    cameraViewModel.loadTestData()

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
            AccountScreen(userViewModel = koinViewModel(), navController = navHostController)
        }
        composable<Routes.CameraDetailsRoute> {
            val cameraRoute = it.toRoute<Routes.CameraDetailsRoute>()
            val cameraBean = cameraViewModel.camerasList.collectAsStateWithLifecycle()
                .value.first { w -> w.id == cameraRoute.id }
            CameraDetailsScreen(
                cameraBean = cameraBean,
                navController = navHostController,
            )
        }
        composable<Routes.CameraStreamRoute> {
            val cameraRoute = it.toRoute<Routes.CameraStreamRoute>()
            val cameraBean = cameraViewModel.camerasList.collectAsStateWithLifecycle()
                .value.first { w -> w.id == cameraRoute.id }
            CameraStreamScreen(
                cameraBean = cameraBean,
            )
        }
        composable<Routes.CameraEndRoute> {
            CameraEndScreen(
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
        composable<Routes.CameraListRoute> {
            CameraListScreen(
                navController = navHostController,
                onNavigateBack = {
                    navHostController.navigate(Routes.CameraEndRoute)
                }
            )
        }
    }
}

package fr.aerisys.mobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.aerisys.mobile.ui.screens.CameraScreen
import fr.aerisys.mobile.ui.screens.HomeScreen
import fr.aerisys.mobile.ui.screens.CameraListScreen
import fr.aerisys.mobile.viewModel.CameraViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

class Routes {
    @Serializable
    data object HomeRoute

    @Serializable
    data object CameraListRoute

    @Serializable
    data class CameraRoute(val id: Long)
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navHostController = rememberNavController()
    val cameraViewModel = koinViewModel<CameraViewModel>()

    NavHost(
        navController = navHostController,
        startDestination = Routes.HomeRoute,
        modifier = modifier
    ) {
        composable<Routes.HomeRoute> {
            HomeScreen(
                onNavigateToCameraList = {
                    navHostController.navigate(Routes.CameraListRoute)
                }
            )
        }
        composable<Routes.CameraRoute> {
            val cameraRoute = it.toRoute<Routes.CameraRoute>()
            val cameraBean = cameraViewModel.camerasList.collectAsStateWithLifecycle()
                .value.first { w -> w.id == cameraRoute.id }
            CameraScreen(
                cameraBean = cameraBean,
            )
        }
        composable<Routes.CameraListRoute> {
            CameraListScreen(
                navController = navHostController,
                viewModel = cameraViewModel
            )
        }
    }
}

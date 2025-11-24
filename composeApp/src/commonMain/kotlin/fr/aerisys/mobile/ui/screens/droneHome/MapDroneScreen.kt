package fr.aerisys.mobile.ui.screens.droneHome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.aerisys.mobile.ui.components.PlatformMap
import fr.aerisys.mobile.viewmodel.droneHome.MapDroneViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapDroneScreen(
    modifier: Modifier = Modifier,
    // Retire la dépendance 'drone: Drone' de la signature si elle n'est pas utilisée ici,
    // ou si ses données sont déjà dans le ViewModel.
    viewModel: MapDroneViewModel = koinViewModel()
) {
    // 🚨 1. COLLECTER L'ÉTAT DYNAMIQUE DU VIEWMODEL
    val uiState by viewModel.state.collectAsState()

    // 🚨 2. L'état UIState contient maintenant TOUTES les données (positions et trajectoire)

    // --- APPEL DE LA CARTE ---
    PlatformMap(
        modifier = modifier.fillMaxSize(),
        positionDrone = uiState.positionDrone,
        positionHome = uiState.positionHome,
        // C'est maintenant la liste qui s'allonge dans le ViewModel
        trajectoire = uiState.trajectoire,
        positionPilote = uiState.positionPilote,
        cameraCoordonnees = uiState.cameraCoordonnees
    )
}
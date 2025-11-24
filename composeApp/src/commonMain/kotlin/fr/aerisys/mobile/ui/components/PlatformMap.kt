package fr.aerisys.mobile.ui.components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class Coordonnees(val latitude: Double, val longitude: Double)

@Composable
expect fun PlatformMap(
    modifier: Modifier = Modifier,
    positionDrone: Coordonnees,
    positionHome: Coordonnees,
    positionPilote: Coordonnees,
    trajectoire: List<Coordonnees>, // Liste des points visités
    cameraCoordonnees: Coordonnees // Centre de la vue
)
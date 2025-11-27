package fr.aerisys.mobile.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
actual fun PlatformMap(
    modifier: Modifier,
    positionDrone: Coordonnees,
    positionHome: Coordonnees,
    trajectoire: List<Coordonnees>,
    cameraCoordonnees: Coordonnees
) {
}
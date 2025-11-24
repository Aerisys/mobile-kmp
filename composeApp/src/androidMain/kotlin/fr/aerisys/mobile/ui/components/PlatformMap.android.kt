package fr.aerisys.mobile.ui.components

// androidMain/src/androidMain/kotlin/ui/components/PlatformMap.android.kt

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun PlatformMap(
    modifier: Modifier,
    // On reçoit les trois positions distinctes
    positionDrone: Coordonnees,
    positionHome: Coordonnees,
    positionPilote: Coordonnees,
    // On reçoit la liste complète de points pour la trace
    trajectoire: List<Coordonnees>,
    cameraCoordonnees: Coordonnees // Point de focus de la caméra
) {
    // 1. Gérer la position de la caméra
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(cameraCoordonnees.latitude, cameraCoordonnees.longitude), 18f
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        // 2. Affichage de la TRAJECTOIRE (ligne complète)
        if (trajectoire.isNotEmpty()) {
            Polyline(
                points = trajectoire.map { LatLng(it.latitude, it.longitude) },
                color = androidx.compose.ui.graphics.Color.Blue, // Couleur du tracé
                width = 5f
            )
        }

        // 3. Affichage des MARQUEURS INDIVIDUELS (qui bougent)

        // Marqueur 1 : POSITION DU DRONE (Icône personnalisée ou couleur)
        Marker(
            state = MarkerState(position = LatLng(positionDrone.latitude, positionDrone.longitude)),
            title = "Drone",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) // Ex: Rouge
        )

        // Marqueur 2 : POINT DE DÉPART / HOME
        Marker(
            state = MarkerState(position = LatLng(positionHome.latitude, positionHome.longitude)),
            title = "Point de Départ",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) // Ex: Vert
        )

        // Marqueur 3 : POSITION DU PILOTE (ou téléphone)
        Marker(
            state = MarkerState(position = LatLng(positionPilote.latitude, positionPilote.longitude)),
            title = "Pilote",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) // Ex: Bleu clair
        )

        // 4. Ligne de TENSION Pilote-Drone (ligne droite pour la distance)
        Polyline(
            points = listOf(
                LatLng(positionPilote.latitude, positionPilote.longitude),
                LatLng(positionDrone.latitude, positionDrone.longitude)
            ),
            color = androidx.compose.ui.graphics.Color.Red.copy(alpha = 0.5f), // Ligne pointillée semi-transparente
            width = 3f,
            // Pour faire une ligne de tension ou d'urgence (optionnel)
        )
    }
}
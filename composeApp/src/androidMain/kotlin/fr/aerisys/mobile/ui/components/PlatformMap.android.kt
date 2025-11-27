package fr.aerisys.mobile.ui.components

// androidMain/src/androidMain/kotlin/ui/components/PlatformMap.android.kt

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.location.Location
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.tasks.await


@Composable
actual fun PlatformMap(
    modifier: Modifier,
    positionDrone: Coordonnees,
    positionHome: Coordonnees,
    trajectoire: List<Coordonnees>
) {
    LocationPermissionHandler(positionDrone,positionHome,trajectoire)
}

@Composable
fun PlatformMapTrue(
    modifier: Modifier,
    positionDrone: Coordonnees,
    positionHome: Coordonnees,
    trajectoire: List<Coordonnees>,
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // État pour stocker la position initiale de la caméra
    var initialCameraCoordonnees by remember { mutableStateOf<Coordonnees?>(null) }

    // NOUVEAU : État pour s'assurer que GoogleMap a fini son initialisation
    var isMapLoaded by remember { mutableStateOf(false) }

    val initialZoom = 18f

    // 1. Charger la position du pilote de manière asynchrone (pour le premier centre)
    LaunchedEffect(Unit) {
        try {
            val location: Location? = fusedLocationClient.lastLocation.await()
            if (location != null) {
                // Utiliser la position du pilote si disponible
                initialCameraCoordonnees = Coordonnees(location.latitude, location.longitude)
            } else {
                // Sinon, utiliser la position du drone
                initialCameraCoordonnees = positionDrone
            }
        } catch (e: SecurityException) {
            initialCameraCoordonnees = positionDrone
        }
    }

    // Afficher l'état de chargement
    if (initialCameraCoordonnees == null) {
        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
            Text("Chargement de la carte...")
        }
        return
    }

    // 2. Initialiser l'état de la caméra avec la position initiale calculée
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(initialCameraCoordonnees!!.latitude, initialCameraCoordonnees!!.longitude),
            initialZoom
        )
    }

    // 3. FORCER LE SUIVI DU DRONE (sans changer le zoom)
    // L'effet dépend de positionDrone ET de l'état isMapLoaded
    LaunchedEffect(positionDrone, isMapLoaded) {
        // GARDE DE SÉCURITÉ CRUCIALE : Sortir si la carte n'est pas prête.
        // Cela empêche la NullPointerException sur CameraUpdateFactory.
        if (!isMapLoaded) return@LaunchedEffect

        val droneLatLng = LatLng(positionDrone.latitude, positionDrone.longitude)

        // On demande un déplacement vers la nouvelle position du drone, en conservant le zoom actuel.
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                droneLatLng,
                cameraPositionState.position.zoom // Conserver le zoom actuel
            ),
            durationMs = 500 // Animation pour un suivi fluide
        )
    }

    // Le composable GoogleMap
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        // NOUVEAU : Déclencher le drapeau de carte chargée
        onMapLoaded = { isMapLoaded = true }
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

        // Marqueur 1 : POSITION DU DRONE
        Marker(
            state = MarkerState(position = LatLng(positionDrone.latitude, positionDrone.longitude)),
            title = "Drone",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )

        // Marqueur 2 : POINT DE DÉPART / HOME
        Marker(
            state = MarkerState(position = LatLng(positionHome.latitude, positionHome.longitude)),
            title = "Point de Départ",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    positionDrone: Coordonnees,
    positionHome: Coordonnees,
    trajectoire: List<Coordonnees>
) {
    // 1. Déclarer l'état des permissions nécessaires
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // 2. Vérifier si toutes les permissions sont accordées
    if (locationPermissionsState.allPermissionsGranted) {
        // LES PERMISSIONS SONT ACCORDÉES : ON PEUT AFFICHER LA CARTE
        PlatformMapTrue(
            modifier = Modifier.fillMaxSize(),
            positionDrone = positionDrone,
            positionHome = positionHome,
            trajectoire = trajectoire
        )
    } else {
        // LES PERMISSIONS NE SONT PAS ACCORDÉES : ON DEMANDE OU ON AFFICHE UN MESSAGE
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                "Veuillez accorder les permissions de localisation pour afficher votre position sur la carte."
            } else {
                "La localisation est essentielle. Veuillez l'activer dans les paramètres de l'application."
            }

            Text(textToShow)

            Spacer(Modifier.height(8.dp))

            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text("Demander la permission GPS")
            }
        }
    }
}
package fr.aerisys.mobile.viewmodel.droneHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // 🚨 NOUVEL IMPORT OBLIGATOIRE
import fr.aerisys.mobile.ui.components.Coordonnees
import kotlinx.coroutines.delay // 🚨 NOUVEL IMPORT OBLIGATOIRE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch // 🚨 NOUVEL IMPORT OBLIGATOIRE
import kotlin.random.Random // Pour la simulation de mouvement

val posLatYnov = 46.32393815381968
val posLongYnov = 5.0081885705627505
data class MapUIState(
    val positionDrone: Coordonnees = Coordonnees(posLatYnov, posLongYnov), // Position de départ simulée
    val positionHome: Coordonnees = Coordonnees(posLatYnov, posLongYnov),
    val trajectoire: List<Coordonnees> = emptyList(),
)

class MapDroneViewModel(): ViewModel() {
    private val _state = MutableStateFlow(MapUIState())
    val state: StateFlow<MapUIState> = _state.asStateFlow()

    init {
        // DÉMARRE LA SIMULATION AUTOMATIQUEMENT LORSQUE LE VIEWMODEL EST CRÉÉ
        startSimulation()
    }

    private fun startSimulation() {
        // Le viewModelScope garantit que la boucle s'arrête si l'écran est détruit
        viewModelScope.launch {
            while(true) {
                // Attendre 1 seconde
                delay(1000L)

                // Générer un nouveau point (simulé) et mettre à jour l'état
                val currentDronePos = _state.value.positionDrone
                val newPos = generateNewPosition(currentDronePos)

                // Mettre à jour l'état de la carte
                updateDronePosition(newPos)
            }
        }
    }

    private fun generateNewPosition(current: Coordonnees): Coordonnees {
        // Petite variation aléatoire pour simuler le mouvement
        val deltaLat = Random.nextDouble(-0.0001, 0.0001)
        val deltaLon = Random.nextDouble(-0.0001, 0.0001)

        return Coordonnees(
            latitude = current.latitude + deltaLat,
            longitude = current.longitude + deltaLon
        )
    }

    // Cette fonction sera appelée par la simulation ou par le vrai SDK
    fun updateDronePosition(newCoord: Coordonnees) {
        _state.update { currentState ->
            currentState.copy(
                positionDrone = newCoord,
                trajectoire = currentState.trajectoire + newCoord
            )
        }
    }
}
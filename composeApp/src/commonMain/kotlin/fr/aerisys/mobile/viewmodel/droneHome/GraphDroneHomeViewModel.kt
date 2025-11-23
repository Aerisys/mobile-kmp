package fr.aerisys.mobile.viewmodel.droneHome

import androidx.lifecycle.ViewModel
import fr.aerisys.mobile.db.AerisysDatabase
import fr.aerisys.mobile.model.Drone
import fr.aerisys.mobile.model.Orientation
import fr.aerisys.mobile.model.SensorData
import fr.aerisys.mobile.model.Vector3
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class GraphDroneHomeViewModel(
    val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    val myDatabase: AerisysDatabase // Si non utilisé, peut être retiré
) : ViewModel() {
    val runInProgress = MutableStateFlow(false)

    // NOUVEAU: Liste des données de capteurs (historique)
    val sensorDataList = MutableStateFlow(emptyList<SensorData>())

    // NOUVEAU: Fonction pour générer des données factices
    fun loadFakeData() {
        runInProgress.value = true

        val fakeData = generateSequence {
            // Génération de valeurs aléatoires pour simuler les capteurs
            SensorData(
                gyroscope = Vector3(
                    x = Random.nextDouble(-10.0, 10.0),
                    y = Random.nextDouble(-10.0, 10.0),
                    z = Random.nextDouble(-10.0, 10.0)
                ),
                accelerometer = Vector3(
                    x = Random.nextDouble(-9.8, 9.8),
                    y = Random.nextDouble(-9.8, 9.8),
                    z = Random.nextDouble(-9.8, 9.8)
                ),
                orientation = Orientation(
                    yaw = Random.nextDouble(0.0, 360.0),
                    roll = Random.nextDouble(-45.0, 45.0),
                    pitch = Random.nextDouble(-45.0, 45.0)
                )
            )
        }
        // Prendre les 50 dernières secondes de données simulées
        sensorDataList.value = fakeData.take(50).toList()

        runInProgress.value = false
    }

    init {
        // Chargement des fausses données au démarrage du ViewModel
        loadFakeData()
    }
}
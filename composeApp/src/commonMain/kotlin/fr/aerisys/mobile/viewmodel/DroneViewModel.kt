package fr.aerisys.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.aerisys.mobile.db.AerisysDatabase
import fr.aerisys.mobile.model.Drone
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DroneViewModel(
    val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    val myDatabase: AerisysDatabase
) : ViewModel() {
    private val queries = myDatabase.dronesQueries
    val drones = MutableStateFlow(emptyList<Drone>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadFakeData(runInProgress :Boolean = false, errorMessage:String = "" ){
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        drones.value =
            listOf(
                Drone(
                    1,
                    100,
                    "DJI Mini 4 Pro",
                    "AA:BB:CC:DD:01",
                    "192.168.1.1",
                    "Auto",
                    80,
                    120,
                    "2024-10-20"
                ),
                Drone(
                    2,
                    100,
                    "Parrot Anafi",
                    "AA:BB:CC:DD:02",
                    "192.168.1.2",
                    "Manual",
                    60,
                    100,
                    "2024-09-14"
                ),
                Drone(
                    3,
                    100,
                    "Autel Evo Lite+",
                    "AA:BB:CC:DD:03",
                    "192.168.1.3",
                    "GPS",
                    70,
                    150,
                    "2024-09-01"
                ),
            )
    }
    fun loadDrones(): Job {
        runInProgress.value = true
        errorMessage.value = ""

        return viewModelScope.launch(dispatcher) {
            try {
                val result = queries.selectAllDrones().executeAsList()
                drones.value = result.map { Drone(it.id, name = it.name) }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erreur inconnue"
            } finally {
                runInProgress.value = false
            }
        }
    }
    fun loadDroneById(idDrone: Long): Job {
        runInProgress.value = true
        errorMessage.value = ""

        return viewModelScope.launch(dispatcher) {
            try {
                val result = queries.selectDroneById(idDrone).executeAsList()
                drones.value = result.map { Drone(it.id,  name=it.name) }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erreur inconnue"
            } finally {
                runInProgress.value = false
            }
        }
    }

    fun addDrone(drone: Drone, onSuccess: () -> Unit) {
        if (drone.name.isEmpty() || drone.name.isBlank()) { // NOUVEAU: Vérification
            errorMessage.value = "Le nom du drone ne peut pas être vide."
            return
        }
        runInProgress.value = true
        errorMessage.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                queries.insertDrone(
                    name = drone.name,
                    mac_address = null,
                    ip_address = null,
                    flight_mode = null,
                    motor_power = null,
                    altitude_limit = null,
                    firmware_version = null,
                    firmware_last_update = null,
                    user_id = 1,
                    added_at = null,
                    updated_at = null
                )
                loadDrones()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erreur inconnue"
            }finally {
                withContext(Dispatchers.Main) { // S'assurer que le runInProgress est mis à jour sur Main
                    runInProgress.value = false
                }
            }
        }
    }
    fun updateDrone(drone: Drone, onSuccess: () -> Unit) {
        if (drone.name.isEmpty() || drone.name.isBlank()) { // NOUVEAU: Vérification
            errorMessage.value = "Le nom du drone ne peut pas être vide."
            return
        }
        runInProgress.value = true
        errorMessage.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                queries.updateDrone(
                    id = drone.id,
                    name = drone.name,
                    mac_address = null,
                    ip_address = null,
                    flight_mode = null,
                    motor_power = null,
                    altitude_limit = null,
                    firmware_version = null,
                    firmware_last_update = null,
                    updated_at = null
                )
                loadDrones()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erreur inconnue"
            }finally {
                runInProgress.value = false
                withContext(Dispatchers.Main) { // S'assurer que le runInProgress est mis à jour sur Main
                    runInProgress.value = false
                }
            }
        }
    }

    fun deleteDrone(drone: Drone) {
        runInProgress.value = true
        errorMessage.value = ""
        viewModelScope.launch(dispatcher) {
            try {
                queries.deleteDroneById(drone.id)
                loadDrones() // Recharge la liste après suppression
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Erreur inconnue"
            }finally {
                runInProgress.value = false
            }
        }
    }
}

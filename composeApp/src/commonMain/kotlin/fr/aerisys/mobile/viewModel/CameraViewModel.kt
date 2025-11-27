package fr.aerisys.mobile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.aerisys.mobile.model.CameraBean
import fr.aerisys.mobile.model.KtorCameraClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    val cameraClient: KtorCameraClient,
) : ViewModel() {
    val camerasList = MutableStateFlow(emptyList<CameraBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage

        camerasList.value = listOf(
            CameraBean(
                id = 1,
                name = "Sacha",
                user_id = 101,
                ip_address = "192.168.1.10",
                mac_address = "00:1A:2B:3C:4D:5E",
                image_format = "JPEG",
                image_quality = "High",
                image_dimension = "1920x1080",
                firmware_version = "v1.2.0",
                firmware_last_update = 1704067200
            ),
            CameraBean(
                id = 2,
                name = "Peter",
                user_id = 102,
                ip_address = "192.168.1.11",
                mac_address = "00:1A:2B:3C:4D:5F",
                image_format = "PNG",
                image_quality = "Medium",
                image_dimension = "1280x720",
                firmware_version = "v1.1.5",
                firmware_last_update = 1706745600
            ),
            CameraBean(
                id = 3,
                name = "Amaury",
                user_id = 103,
                ip_address = "192.168.1.12",
                mac_address = "00:1A:2B:3C:4D:60",
                image_format = "JPEG",
                image_quality = "Low",
                image_dimension = "640x480",
                firmware_version = "v1.0.9",
                firmware_last_update = 1709251200
            ),
            CameraBean(
                id = 4,
                name = "Macron",
                user_id = 104,
                ip_address = "192.168.1.13",
                mac_address = "00:1A:2B:3C:4D:61",
                image_format = "BMP",
                image_quality = "High",
                image_dimension = "2560x1440",
                firmware_version = "v1.3.1",
                firmware_last_update = 1711929600
            ),
            CameraBean(
                id = 5,
                name = "Chirac",
                user_id = 105,
                ip_address = "192.168.1.14",
                mac_address = "00:1A:2B:3C:4D:62",
                image_format = "JPEG",
                image_quality = "Very High",
                image_dimension = "3840x2160",
                firmware_version = "v2.0.0",
                firmware_last_update = 1714521600
            ),
            CameraBean(
                id = 6,
                name = "Lilibet",
                user_id = 106,
                ip_address = "192.168.1.15",
                mac_address = "00:1A:2B:3C:4D:63",
                image_format = "TIFF",
                image_quality = "Ultra",
                image_dimension = "7680x4320",
                firmware_version = "v2.1.3",
                firmware_last_update = 1717200000
            )
        )
    }

    init {
        loadFakeData(runInProgress = false)
    }

    open fun CameraListLoad(name: String = ""): Job = viewModelScope.launch {
        runInProgress.value = true
        errorMessage.value = ""

        try {
            camerasList.value = if (name.isBlank()) {
                camerasList.value // Keep current list
            } else {
                camerasList.value.filter {
                    it.name.contains(name, ignoreCase = true)
                }
            }

            if (camerasList.value.isEmpty()) {
                errorMessage.value = "No camera found with the name \"$name\""
            }
        } catch (e: Exception) {
            errorMessage.value = "Error loading cameras: ${e.message}"
        } finally {
            runInProgress.value = false
        }
    }
}

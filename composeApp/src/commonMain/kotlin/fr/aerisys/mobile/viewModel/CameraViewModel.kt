package fr.aerisys.mobile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.aerisys.mobile.db.AerisysDatabase
import fr.aerisys.mobile.model.CameraBean
import fr.aerisys.mobile.model.toCameraBean
import fraerisysmobile.db.Cameras
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val database: AerisysDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val camerasList = MutableStateFlow(emptyList<CameraBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")
    val addCameraForm = MutableStateFlow(CameraBean())

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage

        camerasList.value = listOf(
            CameraBean(
                id = 1,
                name = "Test Camera 1",
                userId = 1001L,
                ipAddress = "192.168.1.61/stream",
                macAddress = "00:1A:2B:3C:4D:6E",
                imageFormat = "JPEG",
                imageQuality = "High",
                imageDimension = "1920x1080",
                firmwareVersion = "v1.0.0",
                firmwareLastUpdate = 1622505600
            ),
            CameraBean(
                id = 2,
                name = "Kitchen",
                userId = 102,
                ipAddress = "192.168.1.11",
                macAddress = "00:1A:2B:3C:4D:5F",
                imageFormat = "PNG",
                imageQuality = "Medium",
                imageDimension = "1280x720",
                firmwareVersion = "v1.1.5",
                firmwareLastUpdate = 1706745600
            ),
            CameraBean(
                id = 3,
                name = "Living Room",
                userId = 103,
                ipAddress = "192.168.1.12",
                macAddress = "00:1A:2B:3C:4D:60",
                imageFormat = "JPEG",
                imageQuality = "Low",
                imageDimension = "640x480",
                firmwareVersion = "v1.0.9",
                firmwareLastUpdate = 1709251200
            ),
            CameraBean(
                id = 4,
                name = "Grandma's House",
                userId = 104,
                ipAddress = "192.168.1.13",
                macAddress = "00:1A:2B:3C:4D:61",
                imageFormat = "BMP",
                imageQuality = "High",
                imageDimension = "2560x1440",
                firmwareVersion = "v1.3.1",
                firmwareLastUpdate = 1711929600
            ),
            CameraBean(
                id = 5,
                name = "Gate cam",
                userId = 105,
                ipAddress = "192.168.1.14",
                macAddress = "00:1A:2B:3C:4D:62",
                imageFormat = "JPEG",
                imageQuality = "Very High",
                imageDimension = "3840x2160",
                firmwareVersion = "v2.0.0",
                firmwareLastUpdate = 1714521600
            ),
            CameraBean(
                id = 6,
                name = "Lilibet",
                userId = 106,
                ipAddress = "192.168.1.15",
                macAddress = "00:1A:2B:3C:4D:63",
                imageFormat = "TIFF",
                imageQuality = "Ultra",
                imageDimension = "7680x4320",
                firmwareVersion = "v2.1.3",
                firmwareLastUpdate = 1717200000
            ),
            CameraBean(
                id = 7,
                name = "Security cam",
                userId = 106,
                ipAddress = "192.168.1.15",
                macAddress = "00:1A:2B:3C:4D:63",
                imageFormat = "TIFF",
                imageQuality = "Ultra",
                imageDimension = "7680x4320",
                firmwareVersion = "v2.1.3",
                firmwareLastUpdate = 1717200000
            ),
            CameraBean(
                id = 8,
                name = "Philibert",
                userId = 106,
                ipAddress = "192.168.1.15",
                macAddress = "00:1A:2B:3C:4D:63",
                imageFormat = "TIFF",
                imageQuality = "Ultra",
                imageDimension = "7680x4320",
                firmwareVersion = "v2.1.3",
                firmwareLastUpdate = 1717200000
            )
        )
    }

    fun addCamera(newCamera: CameraBean): Job {
        return viewModelScope.launch(ioDispatcher) {
            try {
                val camerasQueries = database.camerasQueries
                val rowUpdated = camerasQueries.insertCamera(
                    user_id = newCamera.userId,
                    name = newCamera.name,
                    mac_address = newCamera.macAddress,
                    ip_address = newCamera.ipAddress,
                    image_format = newCamera.imageFormat,
                    image_quality = newCamera.imageQuality,
                    image_dimension = newCamera.imageDimension,
                    firmware_version = newCamera.firmwareVersion,
                    firmware_last_update = newCamera.firmwareLastUpdate
                )

                println("Added camera: $newCamera")
                println("Row added: $rowUpdated")

                val rows = if (newCamera.userId != null) {
                    database.camerasQueries.selectCamerasByUserId(newCamera.userId).executeAsList()
                } else {
                    database.camerasQueries.selectAllCameras().executeAsList()
                }
                camerasList.value = rows.map { it.toCameraBean() }
            } catch (e: Exception) {
                errorMessage.value = "Error adding camera: ${e.message}"
            } finally {
                // Reset form sur le thread courant (StateFlow thread-safe)
                addCameraForm.value = CameraBean()
            }
        }
    }

    fun load(userId: Long? = null): Job = viewModelScope.launch(ioDispatcher) {
        runInProgress.value = true
        errorMessage.value = ""

        try {
            val camerasQueries = database.camerasQueries

            val rows = if (userId != null) {
                camerasQueries.selectCamerasByUserId(userId).executeAsList()
            } else {
                camerasQueries.selectAllCameras().executeAsList()
            }
            camerasList.value = rows.map { it.toCameraBean() }

            println("Loaded cameras for userId=$userId: ${camerasList.value.size} cameras found")
        } catch (e: Exception) {
            errorMessage.value = "Error loading cameras: ${e.message}"
        } finally {
            runInProgress.value = false
        }
    }

    fun deleteCamera(cameraBean: CameraBean): Job {
        return viewModelScope.launch(ioDispatcher) {
            try {
                val id = cameraBean.id
                if (id != null) {
                    database.camerasQueries.deleteCameraById(id)
                }

                val rows = if (cameraBean.userId != null) {
                    database.camerasQueries.selectCamerasByUserId(cameraBean.userId).executeAsList()
                } else {
                    database.camerasQueries.selectAllCameras().executeAsList()
                }
                camerasList.value = rows.map { it.toCameraBean() }
            } catch (e: Exception) {
                errorMessage.value = "Error deleting camera: ${e.message}"
            }
        }
    }
}

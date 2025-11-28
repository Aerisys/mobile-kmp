package fr.aerisys.mobile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.aerisys.mobile.model.KtorCameraStreamClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

class CameraStreamViewModel(
    private val reader: KtorCameraStreamClient,
) : ViewModel() {

    init {
        println("MJPEG: CameraStreamViewModel created reader=${reader::class.simpleName}")
    }

    private val _frame = MutableStateFlow<ByteArray?>(null)
    val frame = _frame.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    suspend fun startStream(ipAddress: String?) {
        _error.value = null
        if (ipAddress == null) {
            _error.value = "Adresse IP de la caméra invalide"
            return
        }

        println("MJPEG: startStream called with ipAddress=$ipAddress")
        viewModelScope.launch {
            try {
                reader.readStream(ipAddress).collect { img ->
                    _frame.value = img
                }
            } catch (it: Exception) {
                _error.value = "Erreur lors de la lecture du flux: ${it.message}"
            }
        }

        delay(15000)
        if (_frame.value == null && _error.value == null) {
            _error.value = "Pas de flux reçu après 15 secondes"
        }
    }
}
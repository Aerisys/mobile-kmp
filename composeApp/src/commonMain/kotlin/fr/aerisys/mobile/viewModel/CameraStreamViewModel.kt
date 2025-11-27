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

    suspend fun startStream(ipAddress: String) {
        println("MJPEG: startStream called with ipAddress=$ipAddress")
        viewModelScope.launch {
            reader.readStream(ipAddress).collect { img ->
                _frame.value = img
            }
        }

        delay(10000)
        if (_frame.value == null) {
            _error.value = "Pas de flux reçu après 10 secondes"
        }
    }
}
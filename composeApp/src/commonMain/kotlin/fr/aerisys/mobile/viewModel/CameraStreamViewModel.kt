package fr.aerisys.mobile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.aerisys.mobile.model.KtorCameraStreamClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraStreamViewModel(
    private val reader: KtorCameraStreamClient,
) : ViewModel() {

    init {
        println("MJPEG: CameraStreamViewModel created reader=${reader::class.simpleName}")
    }

    private val _frame = MutableStateFlow<ByteArray?>(null)
    val frame = _frame.asStateFlow()

    fun startStream(ipAddress: String) {
        println("MJPEG: startStream called with ipAddress=$ipAddress")
        viewModelScope.launch {
            reader.readStream(ipAddress).collect { img ->
                _frame.value = img
            }
        }
    }
}
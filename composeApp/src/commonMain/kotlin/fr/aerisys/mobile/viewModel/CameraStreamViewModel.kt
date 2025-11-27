package fr.aerisys.mobile.viewModel

import androidx.lifecycle.ViewModel
import fr.aerisys.mobile.model.KtorCameraStreamClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraStreamViewModel(
    private val reader: KtorCameraStreamClient,
) : ViewModel() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _frame = MutableStateFlow<ByteArray?>(null)
    val frame = _frame.asStateFlow()

    fun startStream(ipAddress: String) {
        scope.launch {
            reader.readStream(ipAddress).collect { img ->
                _frame.value = img
            }
        }
    }
}
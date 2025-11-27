package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.aerisys.mobile.model.CameraBean
import fr.aerisys.mobile.model.decodeImage
import fr.aerisys.mobile.viewModel.CameraStreamViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun CameraStreamScreen(
    modifier: Modifier = Modifier,
    cameraBean: CameraBean,
) {
    val cameraStreamViewModel = koinViewModel<CameraStreamViewModel>()

    LaunchedEffect(cameraBean.ip_address) {
        println("MJPEG: CameraStreamScreen LaunchedEffect for ${cameraBean.ip_address}")
        cameraStreamViewModel.startStream(cameraBean.ip_address)
    }

    val frame by cameraStreamViewModel.frame.collectAsState()

    Scaffold(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            frame?.let { bytes ->
                println("UI: received frame bytes=${bytes.size}")
                val imageBitmap = decodeImage(bytes)
                if (imageBitmap != null) {
                    Image(bitmap = imageBitmap, contentDescription = null)
                } else {
                    println("UI: decodeImage returned null for size=${bytes.size}")
                    Text("Erreur décodage image (${bytes.size} octets)")
                }
            } ?: CircularProgressIndicator()
        }
    }
}
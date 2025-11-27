package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
    cameraStreamViewModel.startStream(cameraBean.ip_address)

    val frame by cameraStreamViewModel.frame.collectAsState()

    Scaffold(modifier = modifier) {
        frame?.let { bytes ->
            decodeImage(bytes)?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(90f)
                )
            }
        }
    }
}
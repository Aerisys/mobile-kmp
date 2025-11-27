package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.aerisys.mobile.model.CameraBean
import fr.aerisys.mobile.model.decodeImage
import fr.aerisys.mobile.viewModel.CameraStreamViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraStreamScreen(
    modifier: Modifier = Modifier,
    cameraBean: CameraBean,
    navHostController: NavHostController
) {
    val cameraStreamViewModel = koinViewModel<CameraStreamViewModel>()

    LaunchedEffect(cameraBean.ip_address) {
        println("MJPEG: CameraStreamScreen LaunchedEffect for ${cameraBean.ip_address}")
        cameraStreamViewModel.startStream(cameraBean.ip_address)
    }

    val frame by cameraStreamViewModel.frame.collectAsState()
    val error by cameraStreamViewModel.error.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        if (error == null) {
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 120.dp)
                )
                Spacer(modifier = Modifier.padding(20.dp))
                Text(error!!)
            }
        }
    }
}
package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.aerisys.mobile.model.CameraBean
import fr.aerisys.mobile.viewModel.CameraViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddCameraScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<CameraViewModel>()
    var name by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }
    var imageFormat by remember { mutableStateOf("JPEG") }
    var imageQuality by remember { mutableStateOf("50") }
    var imageDimension by remember { mutableStateOf("1920x1080") }
    var macAddress by remember { mutableStateOf("") }

    val isFormValid = name.isNotBlank() && ipAddress.isNotBlank() && imageQuality.all { it.isDigit() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Camera") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = macAddress,
                onValueChange = { macAddress = it },
                label = { Text("MAC Address (e.g., 00:1A:2B:3C:4D:5E)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imageDimension,
                onValueChange = { imageDimension = it },
                label = { Text("Image Dimension (e.g., 1920x1080)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val newCamera = CameraBean(
                        id = (viewModel.camerasList.value.maxOfOrNull { it.id } ?: 0L) + 1,
                        name = name,
                        ip_address = ipAddress,
                        image_format = imageFormat,
                        image_quality = imageQuality,
                        image_dimension = imageDimension,

                        user_id = 1L, // Example of user
                        mac_address = "MAC-${Random.nextInt(1000, 9999)}", // Generate temporary MAC/Placeholder
                        firmware_version = "1.0.0",
                        firmware_last_update = (Clock.System.now().toEpochMilliseconds() / 1000).toInt(),
                    )
                    viewModel.addCamera(newCamera)
                    onNavigateBack()
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Camera")
            }
        }
    }
}
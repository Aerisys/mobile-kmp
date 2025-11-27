package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.aerisys.mobile.ui.Routes
import fr.aerisys.mobile.viewModel.CameraViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CameraListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<CameraViewModel>()

    val cameras by viewModel.camerasList.collectAsState()
    val isLoading by viewModel.runInProgress.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cameras) { camera ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    " ${camera.name}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("IP: ${camera.ip_address}")
                                Text("Format: ${camera.image_format}")
                                Text("Quality: ${camera.image_quality}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        navController.navigate(Routes.CameraDetailsRoute(camera.id))
                                    }
                                ) {
                                    Text("View Camera Details")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.aerisys.mobile.ui.Routes
import fr.aerisys.mobile.viewModel.CameraViewModel

@Composable
fun CameraListScreen(
    onNavigateBack: () -> Unit,
    navController: NavController,
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier
) {
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
            } else if (errorMessage.isNotEmpty()) {
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
                                Text(" ${camera.name}", style = MaterialTheme.typography.titleMedium)
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
                Button(
                    onClick = {
                        navController.navigate(Routes.AddCameraRoute)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add New Camera")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go to end screen")
                }
                Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }


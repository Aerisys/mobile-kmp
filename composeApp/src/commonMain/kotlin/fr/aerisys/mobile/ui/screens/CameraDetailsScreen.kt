package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.aerisys.mobile.model.CameraBean // Ensure this import is correct
import fr.aerisys.mobile.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailsScreen(
    cameraBean: CameraBean,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Details: ${cameraBean.name}") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))


            DetailItem(label = "IP Address", value = cameraBean.ip_address)
            DetailItem(label = "Image Format", value = cameraBean.image_format)
            DetailItem(label = "Quality", value = cameraBean.image_quality.toString())

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.CameraStreamRoute(cameraBean.id))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Live Stream")
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
    Divider()
}
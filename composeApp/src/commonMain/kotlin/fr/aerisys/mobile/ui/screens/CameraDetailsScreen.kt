package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.aerisys.mobile.model.CameraBean
import fr.aerisys.mobile.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailsScreen(
    cameraBean: CameraBean,
    navController: NavController,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Details: ${cameraBean.name}") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))


            DetailItem(label = "IP Address", value = cameraBean.ipAddress)
            DetailItem(label = "Image Format", value = cameraBean.imageFormat)
            DetailItem(label = "Quality", value = cameraBean.imageQuality)
            if (cameraBean.userId != null) {
                DetailItem(label = "User ID", value = cameraBean.userId.toString())
            }

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
private fun DetailItem(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        if (value == null) {
            Text("N/A", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
    HorizontalDivider()
}
package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.aerisys.mobile.viewModel.CameraViewModel
import fr.aerisys.mobile.viewModel.UserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddCameraScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<CameraViewModel>()
    val userViewModel = koinViewModel<UserViewModel>()
    val formValues by viewModel.addCameraForm.collectAsState()
    val uid = userViewModel.state.value.existingUser?.id
    val scope = rememberCoroutineScope()

    val isFormValid =
        formValues.name?.isNotBlank() == true && formValues.ipAddress?.isNotBlank() == true && formValues.imageQuality?.all { it.isDigit() } == true

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Add New Camera") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = formValues.name ?: "",
                onValueChange = { viewModel.addCameraForm.value = viewModel.addCameraForm.value.copy(name = it) },
                label = { Text("Camera name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formValues.ipAddress ?: "",
                onValueChange = { viewModel.addCameraForm.value = viewModel.addCameraForm.value.copy(ipAddress = it) },
                label = { Text("Camera IP (e.g., 1920x1080)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formValues.macAddress ?: "",
                onValueChange = { viewModel.addCameraForm.value = viewModel.addCameraForm.value.copy(macAddress = it) },
                label = { Text("MAC Address (e.g., 00:1A:2B:3C:4D:5E)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formValues.imageDimension ?: "",
                onValueChange = { viewModel.addCameraForm.value = viewModel.addCameraForm.value.copy(imageDimension = it) },
                label = { Text("Image Dimension (e.g., 1920x1080)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val toInsert = viewModel.addCameraForm.value.copy(userId = uid)
                    val job = viewModel.addCamera(toInsert)
                    scope.launch {
                        job.join()
                        onNavigateBack()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Camera")
            }
        }
    }
}
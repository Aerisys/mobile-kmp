package fr.aerisys.mobile.ui.screens.drone


import aerisys.composeapp.generated.resources.Res
import aerisys.composeapp.generated.resources.back
import aerisys.composeapp.generated.resources.delete
import aerisys.composeapp.generated.resources.drone
import aerisys.composeapp.generated.resources.name
import aerisys.composeapp.generated.resources.validate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.aerisys.mobile.model.Drone
import fr.aerisys.mobile.ui.components.MyButton
import fr.aerisys.mobile.ui.viewmodel.DroneViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneScreen(
    isAdd: Boolean,
    drone: Drone,
    viewModel: DroneViewModel = koinViewModel<DroneViewModel>(),
    onDeleteBack: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf(drone.name) }
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.drone)) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.back))
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ID non modifiable
                OutlinedTextField(
                    value = drone.id.toString(),
                    onValueChange = {},
                    label = { Text("") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nom modifiable
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(Res.string.name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if(errorMessage.isNotEmpty()){
                    Text(errorMessage, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                // Bouton Valider
                MyButton(
                    text = stringResource(Res.string.validate),
                    onClick = {
                        val droneSend = drone.copy(name = name)
                        if(isAdd){viewModel.addDrone(droneSend,onBack)}
                        else {viewModel.updateDrone(droneSend,onBack)}
                        },
                    modifier = Modifier.fillMaxWidth()
                )

                if(!isAdd){
                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton Supprimer
                    MyButton(
                        text = stringResource(Res.string.delete),
                        onClick = {
                                    viewModel.deleteDrone(drone)
                                    onDeleteBack()
                                  },
                        color = Color(0xFFF44336), // rouge
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

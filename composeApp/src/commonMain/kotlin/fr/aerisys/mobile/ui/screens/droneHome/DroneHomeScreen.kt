package fr.aerisys.mobile.ui.screens.droneHome

import aerisys.composeapp.generated.resources.Res
import aerisys.composeapp.generated.resources.back
import aerisys.composeapp.generated.resources.drone
import aerisys.composeapp.generated.resources.edit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import fr.aerisys.mobile.model.Drone
import fr.aerisys.mobile.ui.components.navbar.BottomNavigationBar
import fr.aerisys.mobile.ui.components.navbar.NavBarType
import fr.aerisys.mobile.ui.components.navbar.NavEvent
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroneHomeScreen(
    drone: Drone,
    onBack: () -> Unit = {},
    onEdit: (Drone) -> Unit = {}
) {
    var currentRoute by remember { mutableStateOf("graph") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${stringResource(Res.string.drone)} : ${drone.name}" ) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(drone) }) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(Res.string.edit))
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                barType = NavBarType.DRONE,
                currentRoute = currentRoute,
                onNavEvent = { event ->
                    when (event) {
                        is NavEvent.Navigate -> currentRoute = event.route
                        NavEvent.Stay -> {}
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
                when (currentRoute) {
                    "graph" -> GraphDroneScreen(drone)
                    "3d" -> ThirdDimensionDroneScreen()
                }
            }
        }
    )
}
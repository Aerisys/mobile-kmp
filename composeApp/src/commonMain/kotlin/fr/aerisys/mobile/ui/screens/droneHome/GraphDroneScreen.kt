package fr.aerisys.mobile.ui.screens.droneHome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import fr.aerisys.mobile.model.Drone
import fr.aerisys.mobile.ui.viewmodel.DroneViewModel
import fr.aerisys.mobile.viewmodel.droneHome.GraphDroneHomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GraphDroneScreen(
    drone: Drone,
    viewModel: GraphDroneHomeViewModel = koinViewModel<GraphDroneHomeViewModel>(),
) {
    // Récupérer les données et l'état de chargement
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val runInProgress by viewModel.runInProgress.collectAsState()

    // 1. Déclarer le ModelProducer
    val modelGyro = remember { CartesianChartModelProducer() }
    val modelAccel = remember { CartesianChartModelProducer() }
    val modelOri = remember { CartesianChartModelProducer() }


    // 2. Mettre à jour les données du graphique lorsque sensorDataList change
    LaunchedEffect(sensorDataList) {
        if (sensorDataList.isNotEmpty()) {

            // --- Gyroscope ---
            modelGyro.runTransaction {
                lineSeries {
                    series(sensorDataList.map { it.gyroscope.x.toFloat() })
                    series(sensorDataList.map { it.gyroscope.y.toFloat() })
                    series(sensorDataList.map { it.gyroscope.z.toFloat() })
                }
            }

            // --- Accelerometer ---
            modelAccel.runTransaction {
                lineSeries {
                    series(sensorDataList.map { it.accelerometer.x.toFloat() })
                    series(sensorDataList.map { it.accelerometer.y.toFloat() })
                    series(sensorDataList.map { it.accelerometer.z.toFloat() })
                }
            }

            // --- Orientation ---
            modelOri.runTransaction {
                lineSeries {
                    series(sensorDataList.map { it.orientation.yaw.toFloat() })
                    series(sensorDataList.map { it.orientation.roll.toFloat() })
                    series(sensorDataList.map { it.orientation.pitch.toFloat() })
                }
            }
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Analyse des Capteurs : ${drone.name}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (runInProgress) {
            CircularProgressIndicator()
        } else if (sensorDataList.isNotEmpty()) {

            // --- Graphe 1: Gyroscope (Vitesse angulaire) ---
            GraphCard(title = "Gyroscope (rad/s)") {
                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(),
                    ),
                    modelProducer = modelGyro,
                    modifier = Modifier.height(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Graphe 3: Accéléromètre (Attitude) ---
            GraphCard(title = "Accéléromètre (m/s²)") {
                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(),
                    ),
                    modelProducer = modelAccel,
                    modifier = Modifier.height(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Graphe 3: Orientation (Attitude) ---
            GraphCard(title = "Orientation (Yaw / Roll / Pitch)") {
                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(),
                    ),
                    modelProducer = modelOri,
                    modifier = Modifier.height(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Total des points de données : ${sensorDataList.size}", color = Color.Gray)

        } else {
            Text("Aucune donnée de capteur disponible pour l'affichage des graphiques.")
        }
    }
}

// Le composant utilitaire GraphCard reste inchangé
@Composable
fun GraphCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
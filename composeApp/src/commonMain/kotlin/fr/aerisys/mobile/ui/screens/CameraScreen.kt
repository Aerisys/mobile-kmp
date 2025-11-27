package fr.aerisys.mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.aerisys.mobile.model.CameraBean

/**
 * A screen that
 */
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    cameraBean: CameraBean,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {}
    }
}
package fr.aerisys.mobile.ui.screens

import aerisys.composeapp.generated.resources.Res
import aerisys.composeapp.generated.resources.icon_skylab_light_old_logo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,

) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(40.dp)
                .padding(top = 120.dp)
        ) {
            Column (modifier = Modifier){
                Image(
                    painter = painterResource(Res.drawable.icon_skylab_light_old_logo),
                    contentDescription = null,
                )
                Text("Aerisys",fontSize = 30.sp)
            }

            var textEmail by remember { mutableStateOf("") }

            OutlinedTextField(
                value = textEmail,
                onValueChange = { textEmail = it },
                label = { Text("...") }
            )

            Button(
                onClick = { },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Connexion")
            }

        }
    }
}
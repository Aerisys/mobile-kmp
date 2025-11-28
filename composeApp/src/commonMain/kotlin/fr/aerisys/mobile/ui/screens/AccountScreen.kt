package fr.aerisys.mobile.ui.screens

import aerisys.composeapp.generated.resources.Res
import aerisys.composeapp.generated.resources.icon_skylab_light_logo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.aerisys.mobile.ui.Routes
import fr.aerisys.mobile.viewModel.UserViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val userViewModel = koinViewModel<UserViewModel>()
    var email by remember { mutableStateOf("") }
    val state by userViewModel.state

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Image(
                    painter = painterResource(Res.drawable.icon_skylab_light_logo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFFEFEFE)),
                    contentDescription = "Logo Aerisys",
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                Button(onClick = {
                    userViewModel.checkEmail(email)
                }) {
                    Text("Vérifier l'email")
                }

                Button(onClick = {
                    navController.navigate(Routes.CameraListRoute) {
                        popUpTo(Routes.AccountRoute) { inclusive = true }
                    }
                }) {
                    Text("Passer")
                }
            }


            if (state.emailChecked) {
                when {
                    state.existingUser != null -> Login(email, navController)
                    else -> CreateAccount(email,  navController)
                }
            }
        }
    }
}


@Composable
fun CreateAccount(email: String, navController: NavHostController) {
    val userViewModel = koinViewModel<UserViewModel>()
    var username by remember { mutableStateOf("") }
    var firstPassword by remember { mutableStateOf("") }
    var secondPassword by remember { mutableStateOf("") }
    var errorPasswords by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()


    Text("Entrer un pseudo")
    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Pseudonyme") },
    )

    Text("Entrer votre mot de passe")
    TextField(
        value = firstPassword,
        onValueChange = { firstPassword = it },
        label = { Text("Mot de passe") },
        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
    )

    Text("Entrer à nouveau votre mot de passe")
    TextField(
        value = secondPassword,
        onValueChange = { secondPassword = it },
        label = { Text("Mot de passe") },
        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
    )

    errorPasswords?.let {
        Text(it, color = Color.Red)
    }


    Button(
        onClick = {

            errorPasswords = when {
                firstPassword.isEmpty() -> "Le mot de passe est vide"
                secondPassword.isEmpty() -> "Le second mot de passe est vide"
                firstPassword != secondPassword -> "Les mots de passe ne correspondent pas"
                else -> null
            }

            if (errorPasswords != null) return@Button


            scope.launch {
                val userCreation = userViewModel.createUser(email, firstPassword, username)
                if (userCreation != null) {
                    println("User created: ${userCreation.email}")
                    navController.navigate(Routes.CameraListRoute) {
                        popUpTo(Routes.AccountRoute) { inclusive = true }
                    }
                } else {
                    errorPasswords = "Erreur lors de la création"
                }
            }
        },
    ) {
        Text("Créer son compte")
    }
}

@Composable
fun Login(email: String, navController: NavHostController) {
    val userViewModel = koinViewModel<UserViewModel>()

    var password by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    TextField(
        value = password,
        onValueChange = { password = it; errorPassword = null },
        label = { Text("Mot de passe") },
        visualTransformation = PasswordVisualTransformation(),
    )

    errorPassword?.let {
        Text(it, color = Color.Red)
    }

    Button(
        onClick = {
            if (password.isEmpty()) {
                errorPassword = "Le mot de passe est vide"; return@Button
            }

            scope.launch {
                val userLog = userViewModel.login(email, password)

                if (userLog == null) {
                    errorPassword = "Le mot de passe est incorrect"
                } else {
                    println("User logged: ${userLog.email}")
                    navController.navigate(Routes.CameraListRoute) {
                        popUpTo(Routes.AccountRoute) { inclusive = true }
                    }
                }
            }
        },
    ) {
        Text("Se connecter")
    }

}

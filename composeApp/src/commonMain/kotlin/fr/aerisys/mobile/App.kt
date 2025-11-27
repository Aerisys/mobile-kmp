package fr.aerisys.mobile

import androidx.compose.runtime.Composable
import fr.aerisys.mobile.ui.AppNavigation
import fr.aerisys.mobile.ui.theme.AppTheme
import fr.aerisys.mobile.viewModel.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavigation()
    }
}

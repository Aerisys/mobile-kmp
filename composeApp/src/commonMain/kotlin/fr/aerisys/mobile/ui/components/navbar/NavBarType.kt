package fr.aerisys.mobile.ui.components.navbar

import aerisys.composeapp.generated.resources.Res
import aerisys.composeapp.generated.resources.controller
import aerisys.composeapp.generated.resources.drone
import aerisys.composeapp.generated.resources.graph
import aerisys.composeapp.generated.resources.map
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class NavBarType {
    HOME,
    DRONE
}

sealed class NavBarIcon {
    data class Vector(val icon: ImageVector) : NavBarIcon()
    data class Drawable(val res: DrawableResource) : NavBarIcon()
}

data class NavBarItem(
    val icon: NavBarIcon,
    val labelRes: StringResource,
    val route: String
)

val navBarItemsByType = mapOf(
    NavBarType.HOME to listOf(
        NavBarItem(NavBarIcon.Drawable(Res.drawable.drone), Res.string.drone, "drone"),
        NavBarItem(NavBarIcon.Vector(Icons.Default.SettingsRemote), Res.string.controller, "controller")
    ),
    NavBarType.DRONE to listOf(
        NavBarItem(NavBarIcon.Vector(Icons.Default.Settings), Res.string.graph, "graph"),
        NavBarItem(NavBarIcon.Vector(Icons.Default.Map), Res.string.map, "map")
    )
)

package ec.edu.epn.rq_driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun NavBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val items = listOf("Crear rutas", "Rutas", "Perfil")
    val routes = listOf("crearuta", "favoritas", "perfil")


    val selectedIcons = listOf(
        Icons.Filled.Place,
        Icons.Filled.Edit,
        Icons.Filled.Favorite,
        Icons.Filled.Person
    )
    val unselectedIcons = listOf(
        Icons.Outlined.Place,
        Icons.Outlined.Edit,
        Icons.Outlined.FavoriteBorder,
        Icons.Outlined.Person
    )

    // Obtener la ruta actual correctamente
    val navBackStackEntry = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF264653),
        modifier = modifier.fillMaxWidth().background(Color(0xFF264653))
    ) {
        items.forEachIndexed { index, item ->
            val selected = currentRoute == routes[index]

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selected,
                onClick = {
                    navController.navigate(routes[index]) {
                        popUpTo("crearuta") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color(42, 157, 143),
                    unselectedTextColor = Color(42, 157, 143),
                    selectedIconColor = Color(244, 162, 97),
                    selectedTextColor = Color(244, 162, 97),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

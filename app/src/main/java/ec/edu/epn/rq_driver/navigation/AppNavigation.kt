package ec.edu.epn.rq_driver.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.edu.epn.rq_driver.NavBar
import ec.edu.epn.rq_driver.uin.ExploraScreen
import ec.edu.epn.rq_driver.uin.CreaRutaScreen
import ec.edu.epn.rq_driver.uin.FavoritasScreen
import ec.edu.epn.rq_driver.uin.PerfilScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = { NavBar(navController) }  // ✅ Se asegura que el NavBar esté presente
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "explora",
            modifier = Modifier
                .fillMaxSize()  // ✅ Asegurar que el NavHost use el espacio correctamente
                .padding(innerPadding)  // ✅ Evita que el contenido se superponga con el NavBar
        ) {
            // Pantallas de la barra de navegación
            composable("explora") { ExploraScreen(navController) }
            composable("crearuta") { CreaRutaScreen(navController) }
            composable("favoritas") { FavoritasScreen(navController) }
            composable("perfil") { PerfilScreen(navController) }

            // añadir pantallas adicionales

        }
    }
}

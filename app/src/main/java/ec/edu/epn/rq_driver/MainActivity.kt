package ec.edu.epn.rq_driver

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import ec.edu.epn.rq_driver.navigation.AppNavigation
import ec.edu.epn.rq_driver.ui.theme.Rq_driverTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Rq_driverTheme {
                val navController = rememberNavController() // Inicializa el controlador de navegación
                val logged = remember { mutableStateOf(false) }

                AppNavigation(navController, logged=logged)
            }
        }
    }
}
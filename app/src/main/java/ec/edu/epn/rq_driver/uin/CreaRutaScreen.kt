package ec.edu.epn.rq_driver.uin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.*
import ec.edu.epn.rq_driver.viewmodel.MapViewModel

@Composable
fun CreaRutaScreen(navController: NavController, viewModel: MapViewModel = viewModel()) {
    var startLocation by remember { mutableStateOf(TextFieldValue("")) }
    var endLocation by remember { mutableStateOf(TextFieldValue("")) }
    val apiKey = "@string/google_maps_api_key"  // Asegúrate de tener tu clave de API aquí

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = startLocation,
            onValueChange = { startLocation = it },
            label = { Text("Punto de partida") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endLocation,
            onValueChange = { endLocation = it },
            label = { Text("Destino") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                // viewModel.createRoute(startLocation.text, endLocation.text, apiKey)
                navController.navigate("explora") // Ir a la pantalla Explora
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Crear Ruta")
        }
    }
}

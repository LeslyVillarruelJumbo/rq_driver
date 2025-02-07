package ec.edu.epn.rq_driver.uin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.epn.rq_driver.viewmodel.RutaViewModel

@Composable
fun FavoritasScreen(navController: NavController, rutaViewModel: RutaViewModel = viewModel()) {
    // Obtener las rutas desde el ViewModel
    val rutas2 = rutaViewModel.rutas.collectAsState().value

    // Llamar a la función obtenerRutas al cargar la pantalla
    LaunchedEffect(Unit) {
        rutaViewModel.obtenerRutas("67a3ec2da46723a987f76feb") // Reemplaza "driverId" con el id real del conductor
    }
    val rutasCreadas = mutableListOf<Ruta>()
    var index = 0;
    rutas2.forEach { ruta ->
        index = index + 1
        val nuevaRuta = Ruta("${ruta.routeName} $index", "${ruta.startPoint} - ${ruta.finalPoint}")
        rutasCreadas.add(nuevaRuta)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(29, 53, 87)) // Fondo del contenedor
        .padding(horizontal = 16.dp) // Padding horizontal para la columna principal
    ) {
        // Título
        Text(
            text = "Rutas Creadas",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp, top = 60.dp) // Ajustes de padding para el título
        )

        rutasCreadas.forEach { ruta ->
            RutaCard(ruta = ruta, navController = navController)
        }
    }
}

@Composable
fun RutaCard(ruta: Ruta, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp) // Padding vertical entre las tarjetas
            .fillMaxWidth() // Asegura que la tarjeta ocupe el ancho completo
            .clickable {
                // Navegar a la pantalla de detalles con el nombre de la ruta
                Log.d("Ruta Card", "Navegando a la pantalla de detalles con el nombre: ${ruta.nombre}")
                navController.navigate("ruta_detalle/${ruta.nombre}")
            },
        shape = RoundedCornerShape(8.dp),
        // Fondo de la tarjeta
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(42, 157, 143)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = ruta.nombre, style = MaterialTheme.typography.bodyLarge)
                Text(text = ruta.direccion, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Ir a detalles")
        }
    }
}

data class Ruta(val nombre: String, val direccion: String)

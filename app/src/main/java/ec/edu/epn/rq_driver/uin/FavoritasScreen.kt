package ec.edu.epn.rq_driver.uin

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FavoritasScreen(navController: NavController) {
    val rutas = listOf(
        Ruta("Ruta 1", "Av. America - Cuero y Caicedo"),
        Ruta("Ruta 2", "Av. 10 de Agosto - La Carolina"),
        Ruta("Ruta 3", "Av. Patria - La Joya")
    )

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

        // Mostrar las tarjetas de rutas
        rutas.forEach { ruta ->
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
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Ir a detalles")
        }
    }
}

data class Ruta(val nombre: String, val direccion: String)

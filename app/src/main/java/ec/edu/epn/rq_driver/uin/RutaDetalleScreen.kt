package ec.edu.epn.rq_driver.uin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ec.edu.epn.rq_driver.viewmodel.RutaViewModel

@Composable
fun RutaDetalleScreen(navController: NavController, rutaNombre: String, rutaViewModel: RutaViewModel = viewModel()) {
    // Obtener las rutas desde el ViewModel
    val rutas2 = rutaViewModel.rutas.collectAsState().value

    // Llamar a la función obtenerRutas al cargar la pantalla
    LaunchedEffect(Unit) {
        rutaViewModel.obtenerRutas("67a3ec2da46723a987f76feb") // Reemplaza "driverId" con el id real del conductor
    }

    var route: RutaDetalle = RutaDetalle("Ruta no encontrada", "", "", "", 0)
    var index = 0
    // Definir coordenadas de ejemplo
    var startLatitude = 19.4326 // Ejemplo de latitud de inicio
    var startLongitude = -99.1332 // Ejemplo de longitud de inicio
    var endLatitude = 19.7010 // Ejemplo de latitud de destino
    var endLongitude = -99.1530 // Ejemplo de longitud de destino
    rutas2.forEach { ruta ->
        index = index + 1
        val nombreRuta = "${ruta.routeName} $index"
        if(nombreRuta == rutaNombre) {
            route = RutaDetalle(ruta.routeName, ruta.startPoint, ruta.finalPoint, ruta.departureTime, ruta.availableSeats.toInt())
            startLatitude = ruta.startLat.toDouble()
            startLongitude = ruta.startLong.toDouble()
            endLatitude = ruta.finalLat.toDouble()
            endLongitude = ruta.finalLong.toDouble()
        }
    }



    // Fondo de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(29, 53, 87)) // Fondo del contenedor
            .padding(horizontal = 30.dp, vertical = 30.dp) // Padding horizontal para la columna principal
    ) {
        // Título de "Ruta" con texto en negrita y mayor tamaño
        Text(

            text = "${route.nombre}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize * 1.5f // Título más grande
            ),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Texto de los detalles de la ruta con más separación, negrita y mayor tamaño
        val labelStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.4f // Aumento de tamaño de fuente
        )
        val valueStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.4f // Solo aumentar el tamaño
        )

        // Punto de inicio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente los elementos al centro
        ) {
            Text("Punto inicio:", style = labelStyle, color = Color.White)
            Text(route.puntoInicio, style = valueStyle, color = Color.White)
        }

        // Punto de fin
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(45.dp),
            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente los elementos al centro
        ) {
            Text("Punto fin:", style = labelStyle, color = Color.White)
            Text(route.puntoFin, style = valueStyle, color = Color.White)
        }

        // Hora de salida
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp),
            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente los elementos al centro
        ) {
            Text("Hora salida:", style = labelStyle, color = Color.White)
            Text(route.horaSalida, style = valueStyle, color = Color.White)
        }

        // Asientos disponibles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp),
            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente los elementos al centro
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text("Asientos", style = labelStyle, color = Color.White)
                Text("disponibles:", style = labelStyle, color = Color.White)
            }
            Text(
                route.asientosDisponibles.toString(),
                style = valueStyle,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically) // Centra el valor verticalmente con el texto anterior
            )
        }

        // Mostrar los usuarios suscritos a la ruta
        Text(
            text = "Usuarios Suscritos",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize * 1f // Título más grande
            ),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )



        // Botones con estilo igual al de la tarjeta de FavoritasScreen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Button(
                onClick = {
                    navController.navigate("explora?startLat=$startLatitude&startLng=$startLongitude&endLat=$endLatitude&endLng=$endLongitude")
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Color(42, 157, 143))
            ) {
                Text("Ver Ruta", color = Color.White)
            }

        }
    }
}

data class RutaDetalle(
    val nombre: String,
    val puntoInicio: String,
    val puntoFin: String,
    val horaSalida: String,
    val asientosDisponibles: Int
)

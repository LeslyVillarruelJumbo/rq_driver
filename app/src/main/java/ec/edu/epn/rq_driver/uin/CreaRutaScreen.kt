package ec.edu.epn.rq_driver.uin

import android.app.TimePickerDialog

import android.util.Log

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import ec.edu.epn.rq_driver.viewmodel.MapViewModel
import ec.edu.epn.rq_driver.viewmodel.RutaViewModel
import java.util.Calendar
import ec.edu.epn.rq_driver.model.Ruta
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.firebase.auth.FirebaseUser
import ec.edu.epn.rq_driver.model.Conductor
import ec.edu.epn.rq_driver.viewmodel.PerfilViewModel
import kotlinx.coroutines.delay

@Composable
fun CreaRutaScreen(navController: NavController, rutaViewModel: RutaViewModel = viewModel(), mapViewModel: MapViewModel = viewModel(), usuarioDeFirebase: FirebaseUser? = null, perfilVM: PerfilViewModel) {
    var startPoint by remember { mutableStateOf("") }
    var endPoint by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("00:00") }
    var seats by remember { mutableIntStateOf(1) }
    var showMap by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val conductor by perfilVM.usuarioActual.collectAsState()

    LaunchedEffect(usuarioDeFirebase) {
        usuarioDeFirebase?.let {
            Log.d("CrearRutaScreen", "Intentando recuperar usuario con id → ${usuarioDeFirebase.uid}")
            perfilVM.encontrarUsuarioPorId(usuarioDeFirebase.uid)
            conductor?.let { Log.d("CrearRutaScreen", "Usuario recuperado → $conductor") }
        }
    }

    val context = LocalContext.current

    val placesClient = remember { Places.createClient(context) }
    val token = remember { AutocompleteSessionToken.newInstance() }
    var startCoords by remember { mutableStateOf<LatLng?>(null) }
    var endCoords by remember { mutableStateOf<LatLng?>(null) }
    var suggestions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    fun buscarSugerencias(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                suggestions = response.autocompletePredictions
            }
            .addOnFailureListener { exception ->
                Log.e("Places", "Error al obtener sugerencias: ${exception.message}")
            }
    }

    fun obtenerCoordenadas(placeId: String, isStart: Boolean) {
        val placeRequest = FetchPlaceRequest.newInstance(placeId, listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))

        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { response ->
                val coordenadas = response.place.latLng
                if (coordenadas != null) {
                    if (isStart) startCoords = coordenadas else endCoords = coordenadas
                    Log.d("Coordenadas", "Lat: ${coordenadas.latitude}, Lng: ${coordenadas.longitude}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Places", "Error al obtener coordenadas: ${exception.message}")
            }
    }

    // Mensaje de crear ruta
    val mensajeToast = rutaViewModel.mensajeCrearRuta.collectAsState().value
    val esExito = rutaViewModel.esExito.collectAsState().value

    LaunchedEffect(mensajeToast) {
        mensajeToast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            rutaViewModel.limpiarMensajeCrearRuta() // Limpiar el mensaje después de mostrar el toast
        }
    }

    LaunchedEffect(esExito) {
        Log.d("Navegacion", "esExito: $esExito")
        if (esExito == true) {
            delay(100)
            navController.navigate("favoritas")
            rutaViewModel.limpiarExito()
        } else if (esExito == false) {
            rutaViewModel.limpiarExito()
        }
    }


    // Lógica para mostrar el TimePicker
    val showTimePicker = {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                time = String.format("%02d:%02d", selectedHour, selectedMinute)
            },
            hour, minute, true
        )
        timePicker.show()
    }

    // Estructura principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(29, 53, 87)) // Fondo azul oscuro
    ) {
        Text(
            text = "Ruta",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 60.dp, bottom = 24.dp, start = 16.dp)
        )

        // Campo de búsqueda para el punto de inicio
        TextField(
            value = startPoint,
            onValueChange = {
                startPoint = it
                buscarSugerencias(it)
            },
            label = { Text("Punto de inicio") }
        )

        suggestions.forEach { suggestion ->
            Text(
                text = suggestion.getFullText(null).toString(),
                modifier = Modifier.clickable {
                    startPoint = suggestion.getFullText(null).toString()
                    obtenerCoordenadas(suggestion.placeId, isStart = true)
                    suggestions = emptyList() // Limpiar sugerencias
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de búsqueda para el punto final
        TextField(
            value = endPoint,
            onValueChange = {
                endPoint = it
                buscarSugerencias(it)
            },
            label = { Text("Punto de llegada") }
        )

        suggestions.forEach { suggestion ->
            Text(
                text = suggestion.getFullText(null).toString(),
                modifier = Modifier.clickable {
                    endPoint = suggestion.getFullText(null).toString()
                    obtenerCoordenadas(suggestion.placeId, isStart = false)
                    suggestions = emptyList()
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = time,
            onValueChange = { },
            label = { Text("Hora de salida") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .clickable { showTimePicker() }
        )

        OutlinedTextField(
            value = seats.toString(),
            onValueChange = { seats = it.toIntOrNull() ?: 0 },
            label = { Text("Asientos disponibles") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            trailingIcon = {
                Row {
                    IconButton(
                        onClick = { if (seats > 0) seats -= 1 },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrementar", tint = Color.White)
                    }
                    IconButton(
                        onClick = { seats += 1 },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Incrementar", tint = Color.White)
                    }
                }
            }
        )

        // Fila con dos botones: "Ver Ruta" y "Crear Ruta"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(
                onClick = {
                    if (startPoint.isNotEmpty() && endPoint.isNotEmpty()) {
                        val nuevaRuta = Ruta(
                            horaPartida = time,
                            estadoRuta = false, // Esto puede cambiar dependiendo de la lógica de tu aplicación
                            conductorID = "67a3ec2da46723a987f76feb", // Agrega el ID del conductor
                            longitudInicial = startCoords?.longitude ?: -0.250709,
                            longitudFinal = endCoords?.longitude ?: -0.223709,
                            latitudInicial = startCoords?.latitude ?: -0.250709,
                            latitudFinal = endCoords?.latitude ?: -0.223709,
                            nombreRuta = "Ruta",
                            puntoInicial = startPoint,
                            puntoFinal = endPoint,
                            asientosDisponibles = seats,
                            rutaID = null
                        )

                        // Llamamos a la función para crear la ruta
                        rutaViewModel.crearRuta(nuevaRuta)
                    } else {
                        Toast.makeText(context, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Color(42, 157, 143))
            ) {
                Text(text = "Crear Ruta", color = Color.White)
            }
        }
    }
}
package ec.edu.epn.rq_driver.uin

import android.app.Activity
import android.app.TimePickerDialog
import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import ec.edu.epn.rq_driver.viewmodel.MapViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreaRutaScreen(navController: NavController, mapViewModel: MapViewModel = viewModel()) {
    var startPoint by remember { mutableStateOf("") }
    var endPoint by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("00:00") }
    var seats by remember { mutableStateOf(1) }
    var showMap by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Verificar permisos y obtener la ubicación
    LaunchedEffect(Unit) {
        mapViewModel.checkLocationPermission()
    }

    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()
    val userLocation by mapViewModel.userLocation.collectAsState()

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

    // Callback para cargar el mapa
    val mapCallback = remember {
        object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                googleMap.uiSettings.isZoomControlsEnabled = true

                userLocation?.let { location ->
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title("Ubicación actual")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
                }
            }
        }
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

        // Campos de entrada de texto
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = startPoint,
                onValueChange = { startPoint = it },
                label = { Text("Punto de inicio") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp, top = 16.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(42, 157, 143))
            )

            Button(
                onClick = { navController.navigate("explora") },
                modifier = Modifier
                    .height(48.dp)
                    .width(80.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(text = "IR")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = endPoint,
            onValueChange = { endPoint = it },
            label = { Text("Punto de llegada") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .padding(end = 24.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(42, 157, 143))
        )

        OutlinedTextField(
            value = time,
            onValueChange = { },
            label = { Text("Hora de salida") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .padding(end = 24.dp)
                .clickable { showTimePicker() },
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(42, 157, 143))
        )

        OutlinedTextField(
            value = seats.toString(),
            onValueChange = { seats = it.toIntOrNull() ?: 0 },
            label = { Text("Asientos disponibles") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .padding(end = 24.dp),
            readOnly = true,
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
            },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(42, 157, 143))
        )

        // Fila con dos botones: "Ver Ruta" y "Crear Ruta"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Botón "Ver Ruta"
            Button(
                onClick = {
                    if (startPoint.isNotEmpty() && endPoint.isNotEmpty()) {
                        if (!locationPermissionGranted) {
                            Toast.makeText(context, "Por favor concede el permiso de ubicación", Toast.LENGTH_SHORT).show()
                        } else {
                            showMap = true
                        }
                    } else {
                        Toast.makeText(context, "Por favor ingresa un punto de inicio y un punto final.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Color(42, 157, 143))
            ) {
                Text("Ver Ruta", color = Color.White)
            }

            // Botón "Crear Ruta"
            Button(
                onClick = {
                    // Acción al presionar "Crear Ruta"
                    Toast.makeText(context, "Ruta Creada", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Color(42, 157, 143))
            ) {
                Text("Crear Ruta", color = Color.White)
            }
        }

        // Mostrar el mapa si se presionó el botón "Ver Ruta"
        if (showMap) {
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        onCreate(null)
                        getMapAsync(mapCallback)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
            )
        }
    }
}

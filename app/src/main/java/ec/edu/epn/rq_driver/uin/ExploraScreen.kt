package ec.edu.epn.rq_driver.uin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import ec.edu.epn.rq_driver.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import com.google.maps.model.EncodedPolyline

@Composable
fun ExploraScreen(
    navController: NavController,
    startLat: Float,
    startLng: Float,
    endLat: Float,
    endLng: Float,
    mapViewModel: MapViewModel = viewModel()
) {
    val userLocation by mapViewModel.userLocation.collectAsState()
    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()

    val context = LocalContext.current

    // Verificar permisos y obtener ubicación
    LaunchedEffect(Unit) {
        mapViewModel.checkLocationPermission()
    }

    val coroutineScope = rememberCoroutineScope()

    // Callback para cargar el mapa
    val mapCallback = remember {
        object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                googleMap.uiSettings.isZoomControlsEnabled = true

                // Mostrar la ubicación actual del usuario si está disponible
                userLocation?.let { location ->
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title("Ubicación actual")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
                }

                // Mostrar el marcador para el punto de inicio
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(startLat.toDouble(), startLng.toDouble()))
                        .title("Punto de inicio")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )

                // Mostrar el marcador para el punto de llegada
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(endLat.toDouble(), endLng.toDouble()))
                        .title("Punto de llegada")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )

                // Crear un LatLngBounds que cubra todos los puntos
                val builder = LatLngBounds.Builder()
                userLocation?.let { builder.include(it) } // Agregar la ubicación del usuario
                builder.include(LatLng(startLat.toDouble(), startLng.toDouble())) // Agregar el punto de inicio
                builder.include(LatLng(endLat.toDouble(), endLng.toDouble())) // Agregar el punto de llegada
                val bounds = builder.build()

                // Mover la cámara para que todos los puntos sean visibles
                val padding = 100 // espacio alrededor del área visible
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))

                // Obtener la mejor ruta entre los puntos de inicio y fin
                coroutineScope.launch {
                    try {
                        Log.d("ExploraScreen", "Llamando a obtenerMejorRuta: $startLat, $startLng, $endLat, $endLng")
                        val route = mapViewModel.obtenerMejorRuta(startLat.toDouble(), startLng.toDouble(), endLat.toDouble(), endLng.toDouble())

                        // Verificar la respuesta de la ruta
                        if (route != null) {
                            // Decodificar la ruta
                            val polyline: EncodedPolyline = route.overviewPolyline
                            val path = polyline.decodePath() // Decodificar el polyline
                            Log.d("ExploraScreen", "Ruta obtenida: ${path.joinToString(", ")}")

                            // Añadir la ruta al mapa
                            googleMap.addPolyline(
                                PolylineOptions()
                                    .addAll(path as Iterable<LatLng?>) // Añadir la ruta al mapa
                                    .color(0x220000FF) // Establecer el color de la ruta
                                    .width(10f) // Establecer el ancho de la línea
                            )
                        } else {
                            Log.d("ExploraScreen", "No se pudo obtener una ruta.")
                        }
                    } catch (e: Exception) {
                        Log.e("ExploraScreen", "Error al obtener la ruta: ${e.message}")
                    }
                }
            }
        }
    }

    // Estructura principal
    Column(modifier = Modifier.fillMaxSize()) {
        if (!locationPermissionGranted) {
            Text("Permiso de ubicación no concedido", color = Color.Red)
        } else if (userLocation == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Mostrar el mapa si la ubicación está disponible
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        onCreate(null)
                        getMapAsync(mapCallback)
                    }
                },
                modifier = Modifier
                    .fillMaxSize() // Esto hará que el mapa ocupe todo el espacio disponible
            )
        }
    }
}



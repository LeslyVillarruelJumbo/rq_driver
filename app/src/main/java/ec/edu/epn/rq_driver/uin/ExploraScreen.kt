package ec.edu.epn.rq_driver.uin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import ec.edu.epn.rq_driver.viewmodel.MapViewModel

@Composable
fun ExploraScreen(navController: NavController, mapViewModel: MapViewModel = viewModel()) {
    val userLocation by mapViewModel.userLocation.collectAsState()
    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()

    val context = LocalContext.current

    // Verificar permisos y obtener ubicación
    LaunchedEffect(Unit) {
        mapViewModel.checkLocationPermission()
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
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
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
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

package ec.edu.epn.rq_driver.uin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.*
import ec.edu.epn.rq_driver.viewmodel.MapViewModel

@Composable
fun ExploraScreen(navController: NavController, viewModel: MapViewModel = viewModel()) {
    val userLocation by viewModel.userLocation.collectAsState()
    val nearbyPlaces by viewModel.nearbyPlaces.collectAsState()
    val searchedLocation by viewModel.searchedLocation.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(searchedLocation) {
        searchedLocation?.let {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar lugar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            trailingIcon = {
                Button(onClick = { viewModel.searchLocation(searchQuery.text) }) {
                    Text("Buscar")
                }
            }
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            userLocation?.let { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Mi ubicación"
                )
            }

            nearbyPlaces.forEach { place ->
                Marker(
                    state = rememberMarkerState(position = place),
                    title = "Lugar cercano"
                )
            }

            searchedLocation?.let { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación buscada"
                )
            }
        }
    }
}

package ec.edu.epn.rq_driver.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import android.util.Log

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    // Estado para verificar si el permiso de ubicación fue concedido
    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted

    // Estado para almacenar la ubicación actual del usuario
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    // Estado para almacenar coordenadas de un lugar
    private val _coordinates = MutableStateFlow<LatLng?>(null)
    val coordinates: StateFlow<LatLng?> = _coordinates

    // Estado para el mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Verificar permisos y obtener ubicación
    fun checkLocationPermission() {
        viewModelScope.launch {
            // Verificar si los permisos están concedidos
            val permissionGranted = ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            _locationPermissionGranted.value = permissionGranted

            // Si el permiso fue concedido, obtener la ubicación
            if (permissionGranted) {
                getCurrentLocation()
            }
        }
    }

    // Obtener ubicación actual
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                _userLocation.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    // Obtener coordenadas de una dirección
    fun getCoordinates(address: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addressList = geocoder.getFromLocationName(address, 1)

                // Verificar si la lista de direcciones es válida
                if (!addressList.isNullOrEmpty()) {
                    val location = addressList[0]
                    _coordinates.value = LatLng(location.latitude, location.longitude)
                    _errorMessage.value = null // Limpiamos el mensaje de error
                } else {
                    _coordinates.value = null // No se encontraron coordenadas
                    _errorMessage.value = "No se pudieron obtener las coordenadas para: $address"
                }
            } catch (e: Exception) {
                // Capturar excepciones y loguear para depurar
                Log.e("GeocodingError", "Error al obtener coordenadas: ${e.message}")
                _coordinates.value = null
                _errorMessage.value = "Error al geocodificar la dirección: $address"
            }
        }
    }
}



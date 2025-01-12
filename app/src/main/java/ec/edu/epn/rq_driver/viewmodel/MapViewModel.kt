package ec.edu.epn.rq_driver.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    private val _nearbyPlaces = MutableStateFlow<List<LatLng>>(emptyList())
    val nearbyPlaces: StateFlow<List<LatLng>> = _nearbyPlaces

    private val _searchedLocation = MutableStateFlow<LatLng?>(null)
    val searchedLocation: StateFlow<LatLng?> = _searchedLocation

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                actualizarUbicacion(location)
            }
        }
    }

    init {
        obtenerUbicacionPeriodica()
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacionPeriodica() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Cada 10 segundos
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun actualizarUbicacion(location: Location) {
        val nuevaUbicacion = LatLng(location.latitude, location.longitude)
        _userLocation.value = nuevaUbicacion
        _nearbyPlaces.value = generarPuntosCercanos(nuevaUbicacion)
    }

    // 🌍 Nueva función para buscar direcciones
    fun searchLocation(address: String) {
        val context = getApplication<Application>().applicationContext
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val result = geocoder.getFromLocationName(address, 1)
            result?.firstOrNull()?.let {
                _searchedLocation.value = LatLng(it.latitude, it.longitude)
            }
        } catch (e: Exception) {
            _searchedLocation.value = null  // Si no encuentra nada, reseteamos el estado
        }
    }

    private fun generarPuntosCercanos(ubicacion: LatLng): List<LatLng> {
        return listOf(
            LatLng(ubicacion.latitude + 0.001, ubicacion.longitude + 0.001),
            LatLng(ubicacion.latitude - 0.001, ubicacion.longitude - 0.001),
            LatLng(ubicacion.latitude + 0.002, ubicacion.longitude - 0.002),
            LatLng(ubicacion.latitude - 0.002, ubicacion.longitude + 0.002)
        )
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}

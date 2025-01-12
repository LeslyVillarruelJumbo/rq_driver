import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import ec.edu.epn.rq_driver.api.DirectionsApi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RutaViewModel(application: Application) : AndroidViewModel(application) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val directionsApi = retrofit.create(DirectionsApi::class.java)

    private val _routePoints = mutableListOf<LatLng>()
    val routePoints: List<LatLng> get() = _routePoints

    fun getRoute(start: LatLng, end: LatLng, apiKey: String) {
        val origin = "${start.latitude},${start.longitude}"
        val destination = "${end.latitude},${end.longitude}"

        viewModelScope.launch {
            try {
                val response = directionsApi.getDirections(origin, destination, apiKey).execute()
                if (response.isSuccessful) {
                    val directionsResponse = response.body()
                    directionsResponse?.routes?.firstOrNull()?.legs?.firstOrNull()?.steps?.forEach {
                        val startLocation = LatLng(it.start_location.lat, it.start_location.lng)
                        val endLocation = LatLng(it.end_location.lat, it.end_location.lng)

                        _routePoints.add(startLocation)
                        _routePoints.add(endLocation)

                        // Si tienes una polyline, la puedes usar para obtener todos los puntos intermedios
                        val polyline = decodePolyline(it.polyline.points)
                        _routePoints.addAll(polyline)
                    }
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error fetching route: ${e.message}")
            }
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val polyline = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var shift = 0
            var result = 0
            var byte: Int
            do {
                byte = encoded[index++].toInt() - 63
                result = result or (byte and 0x1f shl shift)
                shift += 5
            } while (byte >= 0x20)
            val deltaLat = if (result and 1 != 0) {
                (result shr 1).inv()
            } else {
                result shr 1
            }
            lat += deltaLat

            shift = 0
            result = 0
            do {
                byte = encoded[index++].toInt() - 63
                result = result or (byte and 0x1f shl shift)
                shift += 5
            } while (byte >= 0x20)
            val deltaLng = if (result and 1 != 0) {
                (result shr 1).inv()
            } else {
                result shr 1
            }
            lng += deltaLng

            val newLatLng = LatLng(lat / 1E5, lng / 1E5)
            polyline.add(newLatLng)
        }
        return polyline
    }
}

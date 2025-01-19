package ec.edu.epn.rq_driver.uin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

/*class RutaMapScreen : FragmentActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var startLatitude: Double = 0.0
    private var startLongitude: Double = 0.0
    private var endLatitude: Double = 0.0
    private var endLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtenemos las coordenadas pasadas por el Intent
        startLatitude = intent.getDoubleExtra("startLatitude", 0.0)
        startLongitude = intent.getDoubleExtra("startLongitude", 0.0)
        endLatitude = intent.getDoubleExtra("endLatitude", 0.0)
        endLongitude = intent.getDoubleExtra("endLongitude", 0.0)

        // Mostrar el mapa dentro de un composable
        setContent {
            MapViewComposable(startLatitude, startLongitude, endLatitude, endLongitude)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Coordenadas de inicio y destino
        val startLocation = LatLng(startLatitude, startLongitude)
        val endLocation = LatLng(endLatitude, endLongitude)

        // Añadir marcadores
        googleMap.addMarker(MarkerOptions().position(startLocation).title("Inicio"))
        googleMap.addMarker(MarkerOptions().position(endLocation).title("Destino"))

        // Mover la cámara al punto de inicio
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12f))

        // Dibujar la línea entre los dos puntos
        googleMap.addPolyline(
            PolylineOptions().add(startLocation, endLocation)
                .width(5f).color(0x7f0000ff)
        )
    }

    // Composable para mostrar el mapa en la interfaz de usuario de Compose
    @Composable
    fun MapViewComposable(startLat: Double, startLng: Double, endLat: Double, endLng: Double) {
        AndroidView(
            factory = { context ->
                val mapFragment = SupportMapFragment.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(android.R.id.content, mapFragment)
                transaction.commit()

                mapFragment.getMapAsync(this)
                mapFragment.view
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}*/

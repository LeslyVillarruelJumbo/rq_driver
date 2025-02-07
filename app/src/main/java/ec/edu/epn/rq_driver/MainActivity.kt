package ec.edu.epn.rq_driver

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.annotation.SuppressLint
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import ec.edu.epn.rq_driver.navigation.AppNavigation
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import ec.edu.epn.rq_driver.navigation.AppNavigation
import ec.edu.epn.rq_driver.ui.theme.Rq_driverTheme
import ec.edu.epn.rq_driver.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
  
    private lateinit var clienteOneTap: SignInClient
    private val authViewModel: AuthViewModel by viewModels()
    private val launcherResultadoOneTap = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { authViewModel.launcherResultadoOneTap(it, lifecycleScope, clienteOneTap) }

    fun iniciarGoogleOneTap() {
        authViewModel.iniciarGoogleOneTap(clienteOneTap, launcherResultadoOneTap)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clienteOneTap = Identity.getSignInClient(this)

        enableEdgeToEdge()
        setContent {
            Rq_driverTheme {
                val navController = rememberNavController() // Inicializa el controlador de navegación

                AppNavigation(navController, authViewModel)
    private lateinit var clienteFusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Google Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBxmMvFa-cnkBefCz59gBC_75DgG4ZyCdw")
        }

        clienteFusedLocation = LocationServices.getFusedLocationProviderClient(this)

        // Solo solicitar permisos si no se han otorgado
        if (!tienePermisosUbicacion()) {
            solicitarPermisosUbicacion()
        } else {
            obtenerUbicacionActual()
        }

        setContent {
            val navController = rememberNavController() // Inicializa el controlador de navegación
            val logged = remember { mutableStateOf(false) }

            AppNavigation(navController, logged = logged)
        }
    }

    private fun tienePermisosUbicacion(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun solicitarPermisosUbicacion() {
        // Verificar si los permisos ya están otorgados
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permisos si no están otorgados
            requestPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            // Si ya se tienen permisos, obtener la ubicación
            obtenerUbicacionActual()
        }
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                // Si se otorgan los permisos, obtener la ubicación
                obtenerUbicacionActual()
            } else {
                Log.e("Permisos", "Permisos de ubicación denegados")
            }
        }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacionActual() {
        // Obtener la última ubicación conocida
        clienteFusedLocation.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitud = location.latitude
                val longitud = location.longitude
                Log.d("Ubicacion", "Latitud: $latitud, Longitud: $longitud")
                // Aquí puedes hacer algo con la ubicación (por ejemplo, actualizar un ViewModel o realizar alguna acción)
            } else {
                Log.e("Ubicacion", "No se pudo obtener la ubicación")



        }
    }
}

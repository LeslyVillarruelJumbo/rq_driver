package ec.edu.epn.rq_driver

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import ec.edu.epn.rq_driver.viewmodel.PerfilViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val perfilViewModel: PerfilViewModel by viewModels()


    private lateinit var clienteGoogleOneTap: SignInClient

    private val googleOneTapLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { authViewModel.googleOneTapLauncher(it, lifecycleScope, clienteGoogleOneTap) }

    fun iniciarGoogleOneTap() {
        authViewModel.arrancarGoogleOneTap(clienteGoogleOneTap, googleOneTapLauncher)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clienteGoogleOneTap = Identity.getSignInClient(this)

        enableEdgeToEdge()
        setContent {
            Rq_driverTheme {
                val navController = rememberNavController() // Inicializa el controlador de navegación

                AppNavigation(navController, authViewModel, perfilViewModel)
            }
        }

    }

}

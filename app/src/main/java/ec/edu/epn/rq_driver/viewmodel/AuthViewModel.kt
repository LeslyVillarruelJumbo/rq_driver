package ec.edu.epn.rq_driver.viewmodel

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import ec.edu.epn.rq_driver.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    //      ATRIBUTOS Y LÓGICA INTERNA del ViewModel

    //  Atributos de clase e inicialización
    private val _usuarioAutenticado = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    //  Inicialización del objeto de gestión de Autenticación de Firebase
    private val firebaseAuth : FirebaseAuth = run {

        var auth: FirebaseAuth? = null

        try {
            auth = FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.d("FirebaseAuth",
                "Hubo un problema al generar instancia de FirebaseAuth. ${e.message}"
            )
        }

        return@run auth!!
    }

    //  Métodoo para evaluar la respuesta de Firebase tras intentar iniciar sesión
    private fun evaluarRespuestaFirebase(res: Task<AuthResult>, origen: String = "") {

        _usuarioAutenticado.value = res.isSuccessful

        if(!res.isSuccessful) when {
            res.exception?.message?.contains("The email address is badly formatted") == true -> {
                _errorMessage.value = "!! Ingrese una dirección de correo válida !!"
            }
            res.exception?.message?.contains("The supplied auth credential is incorrect") == true -> {
                _errorMessage.value = "Correo o contraseña incorrectos"
            }
            else -> {
                _errorMessage.value = res.exception?.message ?: "Error interno desconocido${origen}"
            }
        }
    }

    //  Verificación del estado de sesión desde Firebase en cada reinicio
    private fun verificarSesion() {
        _usuarioAutenticado.value = firebaseAuth.currentUser != null
    }


    //      INICIALIZACIÓN DEL VIEWMODEL

    init { verificarSesion() }


    //      PROPIEDADES Y MÉTODOS ACCESIBLES fuera del ViewModel

    //  Propiedades
    val usuarioAutenticado: StateFlow<Boolean> = _usuarioAutenticado
    val errorMessage: StateFlow<String?> = _errorMessage

    //  Métodoo para inicio de sesión con Firebase mediante credenciales
    fun iniciarSesion(correo: String, contrasena: String) {

        if ( correo.isEmpty() || contrasena.isEmpty() ) {
            _errorMessage.value = "El correo y la contraseña son requeridos"
            return
        }

        _errorMessage.value = null

        viewModelScope.launch {
            firebaseAuth
                .signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener {res -> evaluarRespuestaFirebase(res)}
        }
    }

    //  Métodoo para inicio de sesión con Firebase mediante Google One Tap
    private fun iniciarSesionConGoogle(googleIdToken: String?) {
        if (googleIdToken != null) {
            _errorMessage.value = null

            val credential = GoogleAuthProvider.getCredential(googleIdToken, null)

            viewModelScope.launch {
                firebaseAuth
                    .signInWithCredential(credential)
                    .addOnCompleteListener {res -> evaluarRespuestaFirebase(res, "- GOOGLE")}
            }
        } else {
            _errorMessage.value = "No se recibió un ID Token válido"
        }
    }

    fun launcherResultadoOneTap(
        result: ActivityResult,
        lifecycleScope: LifecycleCoroutineScope,
        clienteOneTap: SignInClient
    ) {
        if (result.resultCode == RESULT_OK) {
            try {
                val credential = clienteOneTap.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken

                Log.e(
                    "GoogleOneTap",
                    "Credenciales recibidas: ${credential.id + credential.displayName + credential.profilePictureUri}"
                )

                if (idToken != null) {
                    lifecycleScope.launch { iniciarSesionConGoogle(idToken) }
                } else {
                    Log.e("GoogleOneTap", "No se recibió un ID Token")
                }
            } catch (e: ApiException) {
                Log.e("GoogleOneTap", "Error al manejar One Tap: ${e.message}")
            }
        } else {
            Log.e("GoogleOneTap", "El usuario canceló el inicio de sesión")
        }
    }

    fun iniciarGoogleOneTap(
        clienteOneTap: SignInClient,
        launcher: ActivityResultLauncher<IntentSenderRequest>,
    ) {
        val signInRequest = BeginSignInRequest
            .builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).build()

        clienteOneTap
            .beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    launcher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                } catch (e: Exception){
                    Log.e("GoogleOneTap", "Error al iniciar el flujo One Tap: ${e.message}")
                }
            }.addOnFailureListener {
                Log.e("GoogleOneTap", "Error al iniciar One Tap: ${it.message}")
            }
    }

    //  Métodoo para cerrar la sesión actual de Firebase (independiente del métodoo de login)
    fun cerrarSesion() {
        firebaseAuth.signOut()
        _usuarioAutenticado.value = false
    }
}
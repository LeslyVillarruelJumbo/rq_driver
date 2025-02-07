package ec.edu.epn.rq_driver.viewmodel

import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import ec.edu.epn.rq_driver.BuildConfig
import ec.edu.epn.rq_driver.model.Conductor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.time.Duration.Companion.days


class AuthViewModel : ViewModel() {

    //      ATRIBUTOS Y LÓGICA INTERNA del ViewModel

    //  Atributos de clase e inicialización
    private val _usuarioAutenticado = MutableStateFlow(false)
    private val _esNuevoUsuario = MutableStateFlow<Boolean?>(null)
    private val _usuarioYaFueRecuperado = MutableStateFlow(false)
    private val _estaCargando = MutableStateFlow(false)
    private val _mensajeDeError = MutableStateFlow<String?>(null)
    private val _usuarioRecuperado = MutableStateFlow<Conductor?>(null)
    private val _credencialesDeGoogle = MutableStateFlow<SignInCredential?>(null)
    private val _usuarioDeFirebase = MutableStateFlow<FirebaseUser?>(null)
    private val _credencialesDeAutenticacion = MutableStateFlow<AuthCredential?>(null)
    private val _tokenDeGoogleID = MutableStateFlow<String?>(null)
    private val _errorAlCrearUsuario = MutableStateFlow<Boolean?>(null)
    private val _usuarioCreado = MutableStateFlow<FirebaseUser?>(null)

    //  Inicialización del objeto de gestión de Autenticación de Firebase
    private val firebaseAuth : FirebaseAuth = run {

        var auth: FirebaseAuth? = null

        try {
            auth = FirebaseAuth.getInstance()
            Log.d("AuthViewModel","${auth.currentUser?.uid}")
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

        if (res.isSuccessful) {
            res.addOnCompleteListener {
                _esNuevoUsuario.value = res.result.additionalUserInfo?.isNewUser
                _estaCargando.value = esNuevoUsuario.value == null
                _usuarioDeFirebase.value = res.result.user
            }
            Log.d("AuthViewModel", "Resultado Firebase:\n" +
                    "\t| uid: ${res.result.user?.uid}\n" +
                    "\t| email: ${res.result.user?.email}\n" +
                    "\t| Nombre: ${res.result.user?.displayName}\n" +
                    "\t| ProviderID: ${res.result.user?.providerId}\n" +
                    "\t| TenantID: ${res.result.user?.tenantId}\n" +
                    "\t| Profile: ${res.result.additionalUserInfo?.profile}\n" +
                    "\t| New?: ${res.result.additionalUserInfo?.isNewUser}")
        } else {
            _estaCargando.value = false
            when {
                res.exception?.message?.contains("The email address is badly formatted") == true -> {
                    _mensajeDeError.value = "!! Ingrese una dirección de correo válida !!"
                }

                res.exception?.message?.contains("The supplied auth credential is incorrect") == true -> {
                    _mensajeDeError.value = "Correo o contraseña incorrectos"
                }

                else -> {
                    _mensajeDeError.value =
                        res.exception?.message ?: "Error interno desconocido${origen}"
                }
            }
        }

//        res.addOnCompleteListener { _isLoading.value = false }
//        _isLoading.value = false
    }

    //  Verificación del estado de sesión desde Firebase en cada reinicio
    private fun verificarSesion() {
        _usuarioAutenticado.value = firebaseAuth.currentUser != null
        firebaseAuth.currentUser?.let {
            _usuarioDeFirebase.value = it
            _esNuevoUsuario.value = false
        }
    }


    //      INICIALIZACIÓN DEL VIEWMODEL

    init { verificarSesion() }


    //      PROPIEDADES Y MÉTODOS ACCESIBLES fuera del ViewModel

    //  Propiedades
    val usuarioAutenticado: StateFlow<Boolean> = _usuarioAutenticado
    val estaCargando: StateFlow<Boolean> = _estaCargando
    val esNuevoUsuario: StateFlow<Boolean?> = _esNuevoUsuario
    val mensajeDeError: StateFlow<String?> = _mensajeDeError
    val usuarioRecuperado: StateFlow<Conductor?> = _usuarioRecuperado
    val usuarioDeFirebase: StateFlow<FirebaseUser?> = _usuarioDeFirebase
    val errorAlCrearUsuario: StateFlow<Boolean?> = _errorAlCrearUsuario
    val usuarioCreado: StateFlow<FirebaseUser?> = _usuarioCreado

    //  Métodoo para inicio de sesión con Firebase mediante credenciales
    fun iniciarSesionConCorreo(correo: String, contrasena: String) {

        if ( correo.isEmpty() || contrasena.isEmpty() ) {
            _mensajeDeError.value = "El correo y la contraseña son requeridos"
            return
        }

        _mensajeDeError.value = null
        _estaCargando.value = true

        viewModelScope.launch {
            firebaseAuth
                .signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { evaluarRespuestaFirebase(it) }
        }
    }

    //  Métodoo para inicio de sesión con Firebase mediante Google One Tap
    private fun iniciarSesionConGoogle(googleIdToken: String?) {
        if (googleIdToken != null) {
            _mensajeDeError.value = null
            _estaCargando.value = true

            val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
            _credencialesDeAutenticacion.value = credential

            viewModelScope.launch {
                firebaseAuth
                    .signInWithCredential(credential)
                    .addOnCompleteListener {res -> evaluarRespuestaFirebase(res, "- GOOGLE")}
            }

//            _isLoading.value = false
        } else {
            _mensajeDeError.value = "No se recibió un ID Token válido"
        }
    }

    fun googleOneTapLauncher(
        result: ActivityResult,
        lifecycleScope: LifecycleCoroutineScope,
        clienteOneTap: SignInClient
    ) {
        _estaCargando.value = false

        if (result.resultCode == RESULT_OK) {
            try {
                val credential = clienteOneTap.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken

                _credencialesDeGoogle.value = credential
                _tokenDeGoogleID.value = idToken

                Log.d(
                    "AuthViewModel",
                    "Credenciales recibidas (Google One Tap):\n" +
                            "\t| ID: ${credential.id}\n" +
                            "\t| Token: ${credential.googleIdToken}\n" +
                            "\t| Password: ${credential.password}\n" +
                            "\t| PublicKey: ${credential.publicKeyCredential}\n" +
                            "\t| Name: ${credential.displayName}\n" +
                            "\t| PictureURL: ${credential.profilePictureUri}"
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

    fun arrancarGoogleOneTap(
        cliente: SignInClient,
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

        val onSuccessListener : (BeginSignInResult) -> Unit = { result ->
            try {
                launcher.launch(
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                )
            } catch (e: Exception){
                Log.e("GoogleOneTap", "Error al iniciar el flujo One Tap: ${e.message}")
            }
        }

        val onFailureListener : (Exception) -> Unit = {
            Log.e("GoogleOneTap", "Error al iniciar One Tap: ${it.message}")
        }

        cliente
            .beginSignIn(signInRequest)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)

        _estaCargando.value = true
    }

    //  Métodoo para cerrar la sesión actual de Firebase (independiente del métodoo de login)
    fun cerrarSesion() {
        firebaseAuth.signOut()
//        _usuarioAutenticado.value = false

        _usuarioAutenticado.value = false
        _mensajeDeError.value = null
        _estaCargando.value = false
        _esNuevoUsuario.value = null
        _usuarioRecuperado.value = null
        _credencialesDeGoogle.value = null
        _usuarioYaFueRecuperado.value = false
        _credencialesDeAutenticacion.value = null
        _tokenDeGoogleID.value = null
    }

    // Métodoo para cancelar proceso de suscripción de nuevo usuario
    fun cancelarProcesoDeRegistro() {

        firebaseAuth.currentUser?.let {

            Log.d("AuthViewModel", "Inicio intento de eliminación de cuenta...")

            try {
                it.reauthenticate(GoogleAuthProvider.getCredential(_tokenDeGoogleID.value, null)).addOnCompleteListener { reauthenticateTask ->
                    if (reauthenticateTask.isSuccessful) {
                        Log.d("AuthViewModel", "Reautentiación exitosa previa a la eliminación")
                        it.delete().addOnCompleteListener {deleteTask ->
                            if (deleteTask.isSuccessful) {
                                Log.d("AuthViewModel", "Eliminación exitosa de la cuenta de Firebase !")
                            } else {
                                Log.e("AuthViewModel","Problema con la eliminación (${deleteTask.exception?.message})")
                            }
                        }
                    } else {
                        Log.e("AuthViewModel", "Problema con la reautenticación (${reauthenticateTask.exception?.message})")
                    }
                }
            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                Log.e("AtuhViewModel", "El usuario necesita autenticarse nuevamente antes de poder proceder con la eliminación ! (${e.message})")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al eliminar usuario en Firebase (${e.message})")
            } finally {
                _usuarioAutenticado.value = false
                _mensajeDeError.value = null
                _esNuevoUsuario.value = null
                _credencialesDeGoogle.value = null
                _usuarioRecuperado.value = null
                _usuarioYaFueRecuperado.value = false
            }

        }

    }

    fun crearUsuarioConCorreo(baseContext: Context, user: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(user, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _errorAlCrearUsuario.value = false
                    Log.d("AuthViewModel", "createUserWithEmail:success")
                    _usuarioCreado.value = firebaseAuth.currentUser
                } else {
                    _errorAlCrearUsuario.value = true
                    Log.w("AuthViewModel", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Falló la creación de usuario",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun recuperarInfoDeNuevoSuscriptor() {
        if (_usuarioYaFueRecuperado.value) return
        Log.d("AuthViewModel", "Iniciando recuperación de usuario...")
        _credencialesDeGoogle.value?.let {
            Log.d("AuthViewModel", "Credenciales existen...")
            firebaseAuth.currentUser?.let {
                Log.d("AuthViewModel", "Usuario actual de FirebaseAtuh existe...")
                Log.d("AuthViewModel", "Usuario Actual:\n" +
                        "\t| uid: ${it.uid}\n" +
                        "\t| providerId: ${it.providerId}\n" +
                        "\t| getIdToken(false).result.token: ${it.getIdToken(false).result.token}\n" +
                        "\t| getIdToken(false).result.expirationTimestamp: ${it.getIdToken(false).result.expirationTimestamp.days}\n" +
                        "\t| getIdToken(false).result.signInProvider: ${it.getIdToken(false).result.signInProvider}\n" +
                        "\t| getIdToken(false).result.claims.entries: ${it.getIdToken(false).result.claims.entries}\n" +
                        "\t| phoneNumber: ${it.phoneNumber}\n" +
                        "\t| metadata: ${it.metadata}\n" +
                        "\t| email: ${it.email}\n" +
                        "\t| displayName: ${it.displayName}\n" +
                        "\t| photoUrl: ${it.photoUrl}\n" +
                        "\t| providerData.providerId's: ${it.providerData[0].providerId} — ${it.providerData[1].providerId}\n")
                _usuarioDeFirebase.value = it
                _usuarioRecuperado.value = Conductor(
//                    firesbaseId = it.getIdToken(false).result.token!!,
                    firesbaseId = it.uid,
                    nombre = _credencialesDeGoogle.value!!.givenName ?: "",
                    apellido = _credencialesDeGoogle.value!!.familyName ?: "",
                    fechaNacimiento = Date(),
                    cedula = "",
                    telefono = it.phoneNumber ?: "",
                    email = it.email!!,
                    autoId = "",
                    numViajes = 0,
                    fotoPerfil = if (it.photoUrl != null) it.photoUrl.toString() else null
                )
            }
        }
        _usuarioYaFueRecuperado.value = true
    }

}

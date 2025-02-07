package ec.edu.epn.rq_driver.uin.loginSignup

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import ec.edu.epn.rq_driver.R
import ec.edu.epn.rq_driver.components.Avatar
import ec.edu.epn.rq_driver.components.Campo
import ec.edu.epn.rq_driver.components.FieldOptions
import ec.edu.epn.rq_driver.components.Input
import ec.edu.epn.rq_driver.components.Utils
import ec.edu.epn.rq_driver.components.getModificador
import ec.edu.epn.rq_driver.model.Conductor
import ec.edu.epn.rq_driver.navigation.AppNavigation
import ec.edu.epn.rq_driver.ui.theme.Verde
import ec.edu.epn.rq_driver.viewmodel.AuthViewModel
import ec.edu.epn.rq_driver.viewmodel.PerfilViewModel
import kotlinx.coroutines.launch
import java.util.Date


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SignUpScreen(
    nav: NavHostController = rememberNavController(),
    usuarioSeHaAutenticado: Boolean = false,
    cancelarSuscripcion: () -> Unit = {},
    recuperarInfoDeGoogle: () -> Unit,
    registrarUsuario: (Conductor) -> Unit,
    registrarUsuarioEnFirebase: (Context, String, String) -> Unit,
    errorAlCrearUsuario: Boolean? = null,
    usuarioRecuperadoConOneTap: Conductor?,
    usuarioDeFirebase: FirebaseUser?,
    usuarioCreado: FirebaseUser?,
    perfilVM: PerfilViewModel
) {

    val firebaseId = remember { mutableStateOf<String?>(null) }
    val nombre = remember { mutableStateOf("") }
    val apellido = remember { mutableStateOf("") }
    val cedula = remember { mutableStateOf("") }
    val celular = remember { mutableStateOf("") }
    val correo = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordConfirmation = remember { mutableStateOf("") }
    val uriFotoDePerfil: MutableState<Uri?> = remember { mutableStateOf(null) }

    val mensajeDeRegistro by perfilVM.mensajeDeSuscripcion.collectAsState()
    var mensajeLocal: String? by remember { mutableStateOf(null) }

    var submitted by remember { mutableStateOf(false) }
    // Hasta que se implemente un verdadero mecanismo de validación:
    var condicion by remember { mutableStateOf(false) }

    val currentContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var columnHeight by remember { mutableIntStateOf(0) }
    val textFieldPositions = remember { mutableMapOf<Int, Int>() }
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val botSystemBar = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val focusedInputIndex = remember { mutableStateOf<Int?>(null) }
    val firstRender = remember { mutableMapOf<Int, Boolean>() }

    val modificador = getModificador(firstRender, textFieldPositions, focusedInputIndex)

    fun handlerRegistro() {

        val validacionNombre = perfilVM.validarCampo(nombre.value)
        val validacionApellido = perfilVM.validarCampo(apellido.value)
        val validacionCedula = perfilVM.validarCampo(cedula.value)
        val validacionCelular = perfilVM.validarCampo(celular.value)
        val validacionCorreo = perfilVM.validarCorreo(correo.value)
        val validacionPassword = perfilVM.validarPassword(password.value)
        val validacionConfirmarPassword = perfilVM.validarConfirmarPassword(password.value, passwordConfirmation.value)

        when {
            validacionNombre.status -> mensajeLocal = "Debe llenar el campo de Nombre"
            validacionApellido.status -> mensajeLocal = "Debe llenar el campo de Apellido"
            validacionCedula.status -> mensajeLocal = "Debe llenar el campo de Cedula"
            validacionCelular.status -> mensajeLocal = "Debe llenar el campo de Teléfono"
            !usuarioSeHaAutenticado && validacionCorreo.status -> mensajeLocal = validacionCorreo.message
            !usuarioSeHaAutenticado && validacionPassword.status -> mensajeLocal = validacionPassword.message
            !usuarioSeHaAutenticado && validacionConfirmarPassword.status -> mensajeLocal = validacionConfirmarPassword.message
            else -> {
                val usuario = Conductor(
                    firesbaseId = firebaseId.value ?: (0..1000).random().toString(),  // TODO
                    nombre = nombre.value,
                    apellido = apellido.value,
                    fechaNacimiento = Date(),
                    cedula = cedula.value,
                    telefono = celular.value,
                    email = correo.value,
                    autoId = null,
                    numViajes = 0,
                    fotoPerfil = if (uriFotoDePerfil.value != null) uriFotoDePerfil.value.toString() else ""
                )
                Log.d("SignUpScreen", "handlerRegistro()  ›  Conductor por registrar:\n\t$usuario")
                if(!usuarioSeHaAutenticado) {
                    registrarUsuarioEnFirebase(currentContext, correo.value, password.value)
                }
                mensajeLocal = null
                condicion = true
            }
        }

//        if(condicion) {
//            submitted = true
//            @Suppress("DEPRECATION")
//            Handler().postDelayed({
//                run {
//                    nav.navigate("login")
//                }
//            },1000)
//        } else {
//            coroutineScope.launch { scrollState.scrollTo(0) }
//        }
        if (!condicion) {
            coroutineScope.launch { scrollState.scrollTo(0) }
        }
    }
    fun handlerBotonAtras() {
        cancelarSuscripcion()
        nav.navigate("login") {
            popUpTo("login") { inclusive = true }
        }
    }
    suspend fun onKeyboardHeightChange() {
        focusedInputIndex.value?.let {

            val yPosition: Int = textFieldPositions[it]!!
            val itemRelativeHeight = columnHeight - yPosition
            val keyboardRealHeight = keyboardHeight - botSystemBar

            if (keyboardRealHeight > itemRelativeHeight) {
                scrollState.scrollTo(keyboardRealHeight)
            }
        }
    }
    fun recuperarDatosDeUsuario() {

        if (usuarioSeHaAutenticado) recuperarInfoDeGoogle()

        usuarioRecuperadoConOneTap?.let {

            if (usuarioDeFirebase == null) return@let

//            uriFotoDePerfil.value = usuarioDeFirebase.photoUrl
//            firebaseId.value = usuarioDeFirebase.uid
            uriFotoDePerfil.value = if (!it.fotoPerfil.isNullOrEmpty()) Uri.parse(it.fotoPerfil) else null
            firebaseId.value = it.firesbaseId
            nombre.value = it.nombre
            apellido.value = it.apellido
            celular.value = it.telefono
            correo.value = it.email
        }

    }

    LaunchedEffect(mensajeDeRegistro) {
        Log.d("SignUpScreen", "Resultado del Registro: $mensajeDeRegistro")
    }
    LaunchedEffect(keyboardHeight) { onKeyboardHeightChange() }
    LaunchedEffect(usuarioSeHaAutenticado, usuarioRecuperadoConOneTap) { recuperarDatosDeUsuario() }
    LaunchedEffect(condicion) {
        Log.d("SignUpScreen", "LaunchedEffect(condicion)  ›  Entró")

        if (errorAlCrearUsuario == true) { return@LaunchedEffect }

        Log.d("SignUpScreen", "LaunchedEffect(condicion)  ›  No encotró error en la creación")

        val fbId: String = if (usuarioCreado == null && usuarioDeFirebase != null) {
            usuarioDeFirebase.uid
        } else usuarioCreado?.uid ?: ""

        Log.d("SignUpScreen", "LaunchedEffect(condicion)  ›  Se recupero el fbId: $fbId")

        val usuario = Conductor(
            firesbaseId = fbId,
            nombre = nombre.value,
            apellido = apellido.value,
            fechaNacimiento = Date(),
            cedula = cedula.value,
            telefono = celular.value,
            email = correo.value,
            autoId = null,
            numViajes = 0,
            fotoPerfil = if (uriFotoDePerfil.value != null) uriFotoDePerfil.value.toString() else ""
        )

        Log.d("SignUpScreen", "LaunchedEffect(condicion)  ›  Se creó el objeto Conductor:\n\t$usuario")

        if (condicion) {

            Log.d("SignUpScreen", "LaunchedEffect(condicion)  ›  Se cumplió la 'condición':\n\t$condicion")

            registrarUsuario(usuario)

            Log.d("SignUpScreen", "LaunchedEffect(condicion)  ›  Se registró al usuario en la BD")

            submitted = true
            @Suppress("DEPRECATION")
            Handler().postDelayed({
                run {
                    nav.navigate("login")
                }
            }, 1000)
        }

    }

    val campos = arrayOf(
        Campo(value = nombre, placeholder = "Nombre(s) *", fieldOptions = FieldOptions.RegularTextField()),
        Campo(value = apellido, placeholder = "Apellido(s) *", fieldOptions = FieldOptions.RegularTextField()),
        Campo(value = cedula,
            placeholder = "Cédula de Identidad *",
            tooltip = stringResource(R.string.tooltip_cedula),
            fieldOptions = FieldOptions.NumericTextField()
        ),
        Campo(value = celular,
            placeholder = "Número de Teléfono *",
            tooltip = stringResource(R.string.tooltip_telefono),
            fieldOptions = if (usuarioSeHaAutenticado) {
                FieldOptions.PhoneTextField(ImeAction.Done) { handlerRegistro() }
            } else {
                FieldOptions.PhoneTextField()
            }
        ),
        Campo(value = correo, placeholder = "Correo Electrónico *", fieldOptions = FieldOptions.EmailTextField(), enabled = !usuarioSeHaAutenticado),
        Campo(value = password, placeholder = "Contraseña *", fieldOptions = FieldOptions.PasswordTextField(), hidden = usuarioSeHaAutenticado),
        Campo(value = passwordConfirmation,
            placeholder = "Confirmar Contraseña *",
            fieldOptions = FieldOptions.PasswordTextField(ImeAction.Done) { handlerRegistro() },
            hidden = usuarioSeHaAutenticado
        )
    )


    Box(Utils.universalModifier) {

        if (submitted) {

            RegistroExitosoScreen(Modifier.align(Alignment.Center))

        } else {

            FormularioDeRegistro(
                onSubmit = { handlerRegistro() },
                uriFotoDePerfil = uriFotoDePerfil.value,
                campos = campos,
                mensajeDeError = mensajeLocal,
                modificadorCampos = modificador,
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(scrollState)
                    .imePadding()
                    .onGloballyPositioned { coordinates ->
                        columnHeight = coordinates.size.height
                    }
            )

            BotonAtras { handlerBotonAtras() }

        }
    }
}

@Composable
fun TextoDeMensajeDeErrorRegistro(errorMessage: String? = null) {
    Text(
        text = if (errorMessage.isNullOrEmpty()) " " else errorMessage,
        color = if (errorMessage.isNullOrEmpty()) Color.Transparent else Color.Red,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotonInfoTooltip(modifier: Modifier, tooltip: String) {
    val tooltipPosition = TooltipDefaults.rememberPlainTooltipPositionProvider()
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    TooltipBox (
        tooltip = {
            Text (
                text = tooltip,
                color = Color(0xFF1D3557),
                modifier = modifier
                    .border(1.dp, Color(0xFF1D3557), RoundedCornerShape(15.dp))
                    .shadow(15.dp, RoundedCornerShape(15.dp))
                    .background(Color(0xFF95A0B0))
                    .fillMaxWidth(.6f)
                    .defaultMinSize(minHeight = 80.dp)
                    .padding(15.dp)
            )
        },
        positionProvider = tooltipPosition,
        state = tooltipState
    ) {
        IconButton(
            onClick = { scope.launch { tooltipState.show() } },
            modifier = Modifier
                .requiredSize(30.dp)
                .offset(15.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Más Información",
                tint = Color(0x77FFFFFF),
                modifier = Modifier.fillMaxSize(.9f)
            )
        }
    }
}
@Composable
fun BotonAtras(clickHandler: () -> Unit) {
    IconButton(modifier = Modifier.offset(x = 10.dp, y = 25.dp),
        onClick = clickHandler
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "Ir atrás",
            tint = Color.White,
            modifier = Modifier.size(25.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FormularioDeRegistro(
    modifier: Modifier = Modifier,
    uriFotoDePerfil: Uri? = null,
    mensajeDeError: String? = null,
    campos: Array<Campo> = emptyArray(),
    modificadorCampos: (Int) -> Modifier = { _ -> Modifier },
    onSubmit: () -> Unit = {}
) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight(.94f)
            .fillMaxWidth(25 / 32f)
            .then(modifier)

    ) {

        Row(Modifier.height(175.dp)) {
            Avatar(uriFotoDePerfil)
        }

        Spacer(Modifier.height(5.dp))

        Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {

            TextoDeMensajeDeErrorRegistro(mensajeDeError)

            campos.mapIndexed { indice, campo ->

                if (campo.hidden) { return@mapIndexed }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        Input(fieldValues = campo, modifier = modificadorCampos(indice))
                    }
                    campo.tooltip?.let {
                        BotonInfoTooltip(
                            Modifier.align(Alignment.CenterVertically),
                            it
                        )
                    }
                }

            }

        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = { onSubmit() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde,
                disabledContainerColor = Color.LightGray
            ),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crear Cuenta") }

    }

}

@Composable
fun RegistroExitosoScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(.75f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Registro Exitoso",
            tint = Verde,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        )
        Text(
            "¡Registro Exitoso!",
            color = Verde,
            fontWeight = FontWeight.Black,
            fontSize = 35.sp
        )
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true, apiLevel = 34,
    device = "spec:width=720px,height=1612px,dpi=320,navigation=buttons", name = "Tekno"
)
@Composable
private fun SignUpPreview() {
    val navController = rememberNavController()
    AppNavigation(navController, AuthViewModel(), PerfilViewModel())
}
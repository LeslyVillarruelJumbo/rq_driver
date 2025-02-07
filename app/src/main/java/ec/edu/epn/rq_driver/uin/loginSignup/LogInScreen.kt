package ec.edu.epn.rq_driver.uin.loginSignup

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ec.edu.epn.rq_driver.R
import ec.edu.epn.rq_driver.components.Input
import ec.edu.epn.rq_driver.components.Utils
import ec.edu.epn.rq_driver.components.Utils.animatePlacement
import ec.edu.epn.rq_driver.navigation.AppNavigation
import ec.edu.epn.rq_driver.ui.theme.Verde
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ec.edu.epn.rq_driver.components.Utils.dropShadow
import ec.edu.epn.rq_driver.viewmodel.AuthViewModel
import ec.edu.epn.rq_driver.viewmodel.PerfilViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(
    nav: NavHostController,
    perfilVM: PerfilViewModel,
    estaCargando: Boolean,
    onAccederConCorreo: (String, String) -> Unit,
    onAccederConGoogle: () -> Unit,
    mensajeDeError: String?
) {

    val conductores = perfilVM.usuariosEncontrados.collectAsState().value
    val conductor = perfilVM.usuarioActual.collectAsState().value

    var botonDeSubmitPresionado by remember { mutableStateOf("") }

    var splashScreen by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val courutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(conductor, conductores) {
        perfilVM.cargarListaDeUsuarios()
        Log.d("LogInScreen", "Conductores: $conductores")
        Log.d("LogInScreen", "Conductores: ${perfilVM.cargarListaDeUsuarios()}")
        perfilVM.encontrarUsuarioPorCorreo("giova.cruz@gmail.com")
        Log.d("LogInScreen", "Conductor: $conductor")
    }
    LaunchedEffect(estaCargando) {
        Log.d("LogInScreen", "Está cargando → $estaCargando")
        if (!estaCargando) botonDeSubmitPresionado = ""
    }
    LaunchedEffect(keyboardHeight) {
        courutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    LaunchedEffect(Unit) {
        delay(750)
        splashScreen = false
    }


    Column(
        modifier = Utils.universalModifier.verticalScroll(scrollState).imePadding(),
        verticalArrangement = if (splashScreen) Arrangement.Center else Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(10.dp))

        LogoDeRouteQ()

        AnimatedVisibility(
            visible = !splashScreen,
            enter = fadeIn() + expandVertically()
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                FormularioDeInicioDeSesion(onAccederConCorreo, mensajeDeError, estaCargando)

                Spacer(Modifier.height(5.dp))

                EnlaceDeRecuperacionDeCuenta(onClick = { if (!estaCargando) nav.navigate("recuperar") })

                Spacer(Modifier.height(10.dp))

            }

        }

        AnimatedVisibility(
            visible = !splashScreen
        ) {

            Column(Modifier.fillMaxWidth(13 / 16f)) {

                BotonAccederConGoogle(
                    onClick = { onAccederConGoogle(); botonDeSubmitPresionado = "google" },
                    enabled = !estaCargando,
                    thisButtonSubmitted = estaCargando && botonDeSubmitPresionado == "google"
                )

                Spacer(Modifier.height(10.dp))

                BotonCrearNuevaCuenta(
                    onClick = { nav.navigate("signup") },
                    enabled = !estaCargando
                )

            }

        }

    }

}


@Composable
fun LogoDeRouteQ() {
    Image(
        painterResource(R.drawable.logo_routeq),
        contentDescription = "Logo de RouteQ",
        Modifier
            .fillMaxWidth(.4f)
            .animatePlacement()
    )
}
@Composable
fun LogoDeGoogle() {

    val logoDeGoogle = painterResource(com.google.android.gms.base.R.drawable.googleg_standard_color_18)

    Image(
        painter = logoDeGoogle,
        contentDescription = "Logo de Google",
        modifier = Modifier
            .fillMaxHeight()
            .dropShadow(blur = 12.dp, spread = 2.dp)
    )

}

@Composable
fun AnimacionDeCarga(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        color = Verde,
        strokeWidth = 2.dp,
        modifier = Modifier
            .size(ButtonDefaults.IconSize * 1.1f)
            .then(modifier)
    )
}

@Composable
fun TextoDeBotonDeGoogle(modifier: Modifier = Modifier) {
    Text(
        text = "Acceder con Google",
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
@Composable
fun TextoDeMensajeDeError(errorMessage: String? = null) {
    Text(
        text = if (errorMessage.isNullOrEmpty()) " " else errorMessage,
        color = if (errorMessage.isNullOrEmpty()) Color.Transparent else Color.Red
    )
}

@Composable
fun EnlaceDeRecuperacionDeCuenta(onClick: () -> Unit = {}) {
    Text(
        text = "¿Olvidaste tu contraseña?",
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = Modifier.clickable { onClick() }
    )
}
@Composable
fun BotonCrearNuevaCuenta(onClick: () -> Unit = {}, enabled: Boolean = true) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Verde),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Verde)
    ) {
        Text("Crear cuenta nueva")
    }
}
@Composable
fun BotonInicioConCorreo(onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Verde),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
    }
}
@Composable
fun BotonAccederConGoogle(
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    thisButtonSubmitted: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Verde),
        modifier = Modifier
            .fillMaxWidth()
            .height(ButtonDefaults.MinHeight)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            LogoDeGoogle()

            if (thisButtonSubmitted) {

                AnimacionDeCarga(Modifier.align(Alignment.Center))

            } else {

                TextoDeBotonDeGoogle(Modifier.align(Alignment.Center))

            }

        }

    }
}

@Composable
fun FormularioDeInicioDeSesion(
    onAccederConCorreo: (String, String) -> Unit,
    mensajeDeError: String?,
    estaCargando: Boolean
) {

    val user = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(13 / 16f)
    ) {

        TextoDeMensajeDeError(mensajeDeError)

        Input(
            placeholder = "Correo",
            value = user,
            error = !mensajeDeError.isNullOrEmpty(),
            enabled = !estaCargando
        )
        Input(
            placeholder = "Contraseña",
            hiddeable = true,
            value = pass,
            error = !mensajeDeError.isNullOrEmpty(),
            enabled = !estaCargando
        )

        Spacer(Modifier.height(4.dp))

        BotonInicioConCorreo(
            onClick = { onAccederConCorreo(user.value, pass.value) },
            enabled = !estaCargando
        )

    }

}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SignInPreview() {
    val navController = rememberNavController()
    AppNavigation(navController, AuthViewModel(), PerfilViewModel())
}

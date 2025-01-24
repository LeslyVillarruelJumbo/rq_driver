package ec.edu.epn.rq_driver.uin

import android.os.Build
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(nav: NavHostController, logged: MutableState<Boolean>) {

    var splashScreen by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val courutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(keyboardHeight) {
        courutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    val submitted = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("<Sin nombre>") }

    LaunchedEffect(true) {
        delay(750)
        splashScreen = false
    }

    Column(
        modifier = Utils.universalModifier.verticalScroll(scrollState).imePadding(),
        verticalArrangement = if (splashScreen) Arrangement.Center else Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!submitted.value) {
            Image(
                painterResource(R.drawable.logo_routeq),
                contentDescription = null,
                Modifier
                    .fillMaxWidth(.4f)
                    .animatePlacement()
            )
            AnimatedVisibility(
                visible = !splashScreen,
                enter = fadeIn() + expandVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SignInForm(submitted, name)
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.clickable { nav.navigate("recuperar") }
                    )
                }
            }
            AnimatedVisibility(!splashScreen) {
                OutlinedButton(
                    onClick = { nav.navigate("signup") },
                    modifier = Modifier.fillMaxWidth(13 / 16f),
                    border = BorderStroke(1.dp, Verde),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Verde)
                ) {
                    Text("Crear cuenta nueva")
                }
            }
        } else {
            LaunchedEffect(true) {
                delay(750)
                logged.value = true
                nav.navigate("explora")
            }
            Column(
                Modifier.fillMaxSize(.75f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Login Exitoso",
                    tint = Verde,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                )
                Text(
                    "¡Bienvenido ${name.value}!",
                    color = Verde,
                    fontWeight = FontWeight.Black,
                    fontSize = 35.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(30.dp))
                Button(onClick = {submitted.value = false}) { Text("Regresar") }
            }
        }
    }
}

@Composable
fun SignInForm(
    submitted: MutableState<Boolean> = remember { mutableStateOf(false) },
    name: MutableState<String> = remember { mutableStateOf("") }
) {

    val usuario = "chewie"
    val contrasenia = "pase123"
    val nombre = "Chewbacca"

    val user = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }

    var error by remember { mutableStateOf(false) }
    var empty by remember { mutableStateOf(true) }
    var wrongCredentials by remember { mutableStateOf(true) }
    var errorString by remember { mutableStateOf("Error!") }

    fun checkIfError(): Boolean {
        empty = user.value.isEmpty() || pass.value.isEmpty()
        if (empty) {
            error = true
            errorString = "Debe llenar todos los campos!"
            return true
        }
        wrongCredentials = user.value != usuario || pass.value != contrasenia
        if (wrongCredentials) {
            error = true
            errorString = "Su usuario o contraseña son incorrectos"
            return true
        }
        error = false
        return false
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(13 / 16f)
    ) {
        Text(errorString, color = if (error) Color.Red else Color.Transparent)
        Input("Nombre de usuario, correo", value = user, error = error)
        Input("Contraseña", true, value = pass, error = error)
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = {
                submitted.value = !checkIfError()
                if (submitted.value) name.value = nombre
            },
            colors = ButtonDefaults.buttonColors(containerColor = Verde),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SignInPreview() {
    val navController = rememberNavController()
    AppNavigation(navController)
}
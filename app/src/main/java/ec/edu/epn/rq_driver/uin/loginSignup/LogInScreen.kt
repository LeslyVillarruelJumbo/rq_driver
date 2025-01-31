package ec.edu.epn.rq_driver.uin.loginSignup

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(
    nav: NavHostController,
    onLoginSuccess: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    errorMessage: String?
) {

    var splashScreen by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val courutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(keyboardHeight) {
        courutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    LaunchedEffect(true) {
        delay(750)
        splashScreen = false
    }

    Column(
        modifier = Utils.universalModifier.verticalScroll(scrollState).imePadding(),
        verticalArrangement = if (splashScreen) Arrangement.Center else Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(10.dp))

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

                SignInForm(onLoginSuccess, errorMessage)

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.clickable { nav.navigate("recuperar") }
                )

                Spacer(Modifier.height(10.dp))
            }
        }

        AnimatedVisibility(!splashScreen) {
            Column (Modifier.fillMaxWidth(13 / 16f)) {

                Button(
                    onClick = onGoogleLogin,
                    colors = ButtonDefaults.buttonColors(containerColor = Verde),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ButtonDefaults.MinHeight)
                ) {

                    Box(modifier = Modifier.fillMaxSize()) {

                        Image(
                            painter = painterResource(com.google.android.gms.base.R.drawable.googleg_standard_color_18),
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .fillMaxHeight()
                                .dropShadow(blur = 12.dp, spread = 2.dp)
                        )

                        Text(
                            text = "Iniciar con Google",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { nav.navigate("signup") },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Verde),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Verde)
                ) {
                    Text("Crear cuenta nueva")
                }
            }
        }
    }
}

@Composable
fun SignInForm(
    onLoginSuccess: (String, String) -> Unit,
    errorMessage: String?
) {

    val user = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(13 / 16f)
    ) {

        Text(
            text = if (errorMessage.isNullOrEmpty()) " " else errorMessage,
            color = if (errorMessage.isNullOrEmpty()) Color.Transparent else Color.Red
        )

        Input("Correo", value = user, error = !errorMessage.isNullOrEmpty())
        Input("Contraseña", true, value = pass, error = !errorMessage.isNullOrEmpty())

        Spacer(Modifier.height(4.dp))

        Button(
            onClick = { onLoginSuccess(user.value, pass.value) },
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
    AppNavigation(navController, authViewModel = AuthViewModel())
}
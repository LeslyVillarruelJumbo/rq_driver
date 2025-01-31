package ec.edu.epn.rq_driver.uin.loginSignup

import android.os.Build
import androidx.annotation.RequiresApi
import ec.edu.epn.rq_driver.components.Input
import ec.edu.epn.rq_driver.components.Utils
import ec.edu.epn.rq_driver.navigation.AppNavigation
import ec.edu.epn.rq_driver.ui.theme.Verde
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ec.edu.epn.rq_driver.viewmodel.AuthViewModel

@Composable
fun RecuperarCuentaScreen(nav: NavHostController) {
    val mail = remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Utils.universalModifier,
        verticalArrangement = Arrangement.spacedBy(70.dp)
    ) {
        IconButton(onClick = { nav.navigateUp() }, Modifier.offset(10.dp)) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Ir atrás",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(13 / 16f)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!submitted) {
                Text(
                    "Por favor, ayúdanos con tu correo electrónico para que podamos ayudarte a recuperar tu cuenta :)",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Input(
                    "Correo Electrónico",
                    value = mail
                )
                Button(
                    onClick = { submitted = true },
                    enabled = mail.value.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Verde,
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Enviar Enlace de Recuperación",
                        fontWeight = FontWeight.W400,
                        fontSize = 14.sp
                    )
                }
            } else {
                Text(
                    "¡Muchas gracias!",
                    textAlign = TextAlign.Left,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    color = Color.White,
                    fontSize = 20.sp,
                    text = buildAnnotatedString {
                        append("Si estás registrado con nosotros, recibirás un mensaje en el buzón del correo electrónico con el que te registraste. ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.ExtraLight)) {
                            append("(Recuerda revisar la carpeta de spam en caso de no ver nuestro mensaje)")
                        }
                    }
                )
                Button(
                    onClick = { nav.navigateUp() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Verde,
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Iniciar Sesión",
                        fontWeight = FontWeight.W400,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun RetrieveAccountPreview() {
    val navController = rememberNavController()
    AppNavigation(navController, AuthViewModel())
}
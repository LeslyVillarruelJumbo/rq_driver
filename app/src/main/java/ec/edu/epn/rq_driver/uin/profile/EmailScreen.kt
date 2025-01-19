package ec.edu.epn.rq_user.uin.profile

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ec.edu.epn.rq_user.model.Usuario
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserEmailScreen(navController: NavController) {
  val formatter = ofPattern("MM/dd/yyyy")
  val mockUser = Usuario(
    nombre = "Sebastian",
    apellido = "Cruz",
    email = "sebas.cruz750@gmail.com",
    telefono = "0978601625",
    cedula = "1719356006",
    fechaNacimiento = LocalDate.parse("04/11/2000", formatter)
  )

  Box(
    modifier = Modifier
      .fillMaxSize() // Llenar toda la pantalla
      .background(color = Color(29, 53, 87)) // Fondo con tu color específico
  ) {
    UserEmail(email = "sebas.cruz750@gmail.com", navController)
  }
}


@Composable
fun UserEmail(email: String, navController: NavController) {
  var inputEmail by remember { mutableStateOf("") } // Estado para el campo de texto

  Column(
    modifier = Modifier
      .padding(top = 55.dp)
      .padding(horizontal = 20.dp)
      .wrapContentHeight()
  ) {
    // Fila superior con ícono y textos
    Row(
      modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
    ) {
      Column {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "go-back",
          tint = Color.White,
          modifier = Modifier
            .clickable(onClick = { navController.navigate("informacion") })
        )
        Column(
          modifier = Modifier
            .wrapContentHeight()
        ) {


          Text(
            text = "Correo",
            color = Color(red = 231, green = 111, blue = 81),
            fontSize = 47.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(1000),
            fontFamily = FontFamily.SansSerif,
          )


          Text(
            text = "electrónico",
            color = Color(red = 231, green = 111, blue = 81),
            fontSize = 47.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(1000),
            fontFamily = FontFamily.SansSerif,
          )

        }
      }
    }

    // Texto explicativo
    Row(
      modifier = Modifier
        .padding(top = 10.dp)
    ) {
      Text(
        text = "Este correo te servirá para iniciar sesión,\n" +
             "recibir notificaciones y recuperar tu cuenta",
        color = Color.White,
        fontSize = 14.sp,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Light,
        fontFamily = FontFamily.SansSerif,
      )
    }

    // Campo de entrada para el nombre
    Column(
      modifier = Modifier
        .padding(top = 80.dp)
        .fillMaxWidth()

    )
    {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 10.dp)
      ) {

        Text(
          text = "Correo electrónico",
          color = Color.White,
          fontSize = 20.sp,
        )
      }
      Row(
        Modifier.fillMaxWidth(),
        Arrangement.Center
      ) {

        TextField(

          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .height(55.dp)
            .align(Alignment.CenterVertically)
            .fillMaxWidth(),
          value = inputEmail,
          onValueChange = { newText -> inputEmail = newText },
          placeholder = {
            Text(
              fontSize = 14.sp,
              text = email,
              color = Color.Gray,
              fontWeight = FontWeight.ExtraLight
            )
          },
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(38, 79, 83),
            focusedContainerColor = Color(38, 79, 83),
            focusedTextColor = Color.White,
            cursorColor = Color.Gray,

            ),

          )
      }

    }
    Button(
      modifier = Modifier
        .padding(top = 120.dp)
        .width(311.dp)
        .height(46.dp)
        .align(Alignment.CenterHorizontally),
      onClick = {navController.popBackStack()},
      colors = ButtonDefaults.buttonColors(
        containerColor = Color(42, 157, 143),
      )
    )
    {
      Text(
        text = "Actualizar",

      )
    }
  }
}


@Preview(showBackground = false)
@Composable
fun GreetingPreview6() {

  UserEmail(email = "sebas.cruz750@gmail.com", rememberNavController())

}
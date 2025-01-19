package ec.edu.epn.rq_user.uin.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ec.edu.epn.rq_user.R
import ec.edu.epn.rq_user.model.Usuario
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserInfoScreen(navController: NavController) {
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

    InfoUser(
      name = "${mockUser.nombre} ${mockUser.apellido}",
      phone = mockUser.telefono,
      email = mockUser.email,
      navController,
    )
  }
}


@Composable
fun InfoUser(name: String, phone: String, email: String, navController: NavController) {
  Column(
    modifier = Modifier
      .padding(top = 55.dp)
      .padding(horizontal = 20.dp)
  ) {

    Row(
      modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
    )
    {
      Column {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "go-back",
          tint = Color.White,
          modifier = Modifier
            .clickable(onClick = { navController.navigate("configuracion") })
        )
        Text(
          text = "Información de",
          color = Color(red = 178, green = 34, blue = 45),
          fontSize = 47.sp,
          textAlign = TextAlign.Start,
          fontWeight = FontWeight(1000),
          fontFamily = FontFamily.SansSerif,
        )
        Text(
          text = "la cuenta",
          color = Color(red = 178, green = 34, blue = 45),
          fontSize = 47.sp,
          textAlign = TextAlign.Start,
          fontWeight = FontWeight(1000),
          fontFamily = FontFamily.SansSerif,
        )
        Row(
          Modifier.padding(vertical = 25.dp)
        ) {

          Image(
            modifier = Modifier
              .clip(RoundedCornerShape(100.dp))
              .size(100.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.pfp),
            contentDescription = "Foto de perfil de $name",
          )
        }
        Row {
          Text(
            text = "Información Básica",
            color = Color(red = 231, green = 111, blue = 81),
            fontSize = 32.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(600),
            fontFamily = FontFamily.SansSerif,
          )
        }
        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          Column {
            Text(
              text = "Nombre",
              fontSize = 20.sp,
              textAlign = TextAlign.Start,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.SansSerif,
              color = Color.White,
            )
            Text(
              text = name,
              fontSize = 14.sp,
              textAlign = TextAlign.Start,
              fontWeight = FontWeight.Light,
              fontFamily = FontFamily.SansSerif,
              color = Color.White,
            )
          }
          Column(
            modifier = Modifier
              .align(alignment = Alignment.CenterVertically)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
              contentDescription = "more-info",
              tint = Color.White,
              modifier = Modifier
                .size(25.dp)
                .clickable(onClick = { navController.navigate("updateNombre") })
            )
          }
        }
        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          Column {
            Text(
              text = "Número de teléfono",
              fontSize = 20.sp,
              textAlign = TextAlign.Start,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.SansSerif,
              color = Color.White,
            )
            Text(
              text = phone,
              fontSize = 14.sp,
              textAlign = TextAlign.Start,
              fontWeight = FontWeight.Light,
              fontFamily = FontFamily.SansSerif,
              color = Color.White,
            )
          }
          Column(
            modifier = Modifier
              .align(alignment = Alignment.CenterVertically)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
              contentDescription = "more-info",
              tint = Color.White,
              modifier = Modifier
                .size(25.dp)
                .clickable(onClick = {navController.navigate("updateTelefono")})

            )
          }
        }
        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          Column {
            Text(
              text = "Correo electrónico",
              fontSize = 20.sp,
              textAlign = TextAlign.Start,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.SansSerif,
              color = Color.White,
            )
            Text(
              text = email,
              fontSize = 14.sp,
              textAlign = TextAlign.Start,
              fontWeight = FontWeight.Light,
              fontFamily = FontFamily.SansSerif,
              color = Color.White,
            )
          }
          Column(
            modifier = Modifier
              .align(alignment = Alignment.CenterVertically)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
              contentDescription = "more-info",
              tint = Color.White,
              modifier = Modifier
                .size(25.dp)
                .clickable(onClick = {navController.navigate("updateEmail")})

            )
          }
        }
      }
    }
  }
}


@Preview(showBackground = false)
@Composable
fun GreetingPreview3() {

  InfoUser(
    "Sebastian Cruz",
    phone = "0978601625",
    email = "sebas.cruz750@gmail.com",
    rememberNavController()
  )

}
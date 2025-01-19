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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.HorizontalDivider
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
fun UserSettingsScreen(navController: NavController) {
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

    SettingsUser(
      name = "${mockUser.nombre} ${mockUser.apellido}",
      mockUser.telefono,
      mockUser.email,
      navController
    )
  }
}

@Composable
fun SettingsUser(name: String, phone: String, email: String, navController: NavController) {
  Column(
    modifier = Modifier
      .padding(top = 55.dp)
      .padding(horizontal = 20.dp)
  ) {

    Row(
      modifier = Modifier
        .height(90.dp)
        .fillMaxWidth()
    )
    {
      Column {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "go-back",
          tint = Color.White,
          modifier = Modifier
            .clickable(onClick = {navController.navigate("perfil")})
        )
        Text(
          text = "Configuración",
          color = Color(red = 178, green = 34, blue = 45),
          fontSize = 47.sp,
          textAlign = TextAlign.Start,
          fontWeight = FontWeight(1000),
          fontFamily = FontFamily.SansSerif,
        )
      }
    }
    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 25.dp)
    )
    {
      // COLUMNAS DE USUARIO
      Column {
        Row {
          Column {
            Image(
              modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .size(75.dp), // Tamaño de la imagen
              contentScale = ContentScale.Crop,
              painter = painterResource(id = R.drawable.pfp),
              contentDescription = "Foto de perfil de $name"
            )
          }

          Spacer(modifier = Modifier.width(20.dp))
          Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
              .align(Alignment.CenterVertically)
          ) {
            Text(
              text = name,
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = Color.White,
              textAlign = TextAlign.Left
            )
            Text(
              text = phone,
              fontWeight = FontWeight.Normal,
              fontSize = 14.sp,
              color = Color.White
            )
            Text(
              text = email,
              fontWeight = FontWeight.Normal,
              fontSize = 14.sp,
              color = Color.White
            )
          }
        }
      }

      Column(
        modifier = Modifier
          .align(Alignment.CenterVertically) // Alinea verticalmente el icono
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
          contentDescription = "more-info",
          tint = Color.White,
          modifier = Modifier
            .clickable(onClick = { navController.navigate("informacion") })
            .size(25.dp)
        )
      }
    }

    // DIVISOR
    HorizontalDivider(
      modifier = Modifier
        .padding(vertical = 25.dp)
        .fillMaxWidth(),
//        .width(372.dp),
      color = Color(38, 70, 83),
      thickness = 2.dp

    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(bottom = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // COLUMNAS DE DIRECCIÓN
      Column(
        modifier = Modifier
          .align(Alignment.CenterVertically)
      )
      {
        Row {
          Column(
            verticalArrangement = Arrangement.Center
          ) {

            Icon(
              imageVector = Icons.Outlined.Home,
              contentDescription = "home-logo",
              tint = Color.White,
              modifier = Modifier
                .size(50.dp)
            )
          }
          Spacer(modifier = Modifier.width(20.dp))
          Column(
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Dirección de casa",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = Color.White,
              textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "I Arteta",
              fontWeight = FontWeight.Normal,
              fontSize = 14.sp,
              color = Color.White
            )
          }
        }
      }

      Column(
        modifier = Modifier
          .align(Alignment.CenterVertically) // Alinea verticalmente el icono
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
          contentDescription = "more-info",
          tint = Color.White,
          modifier = Modifier
            .size(25.dp)
            .clickable(onClick = {navController.navigate("updateHouse")})
        )
      }

    }
    // DIRECCIONES EXTRA
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(bottom = 20.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier.align(Alignment.CenterVertically)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = "home-logo",
            tint = Color.White,
            modifier = Modifier.size(50.dp)
          )
          Spacer(modifier = Modifier.width(20.dp))
          Text(
            text = "Direcciones extra",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Start
          )
        }
      }
      Column(
        modifier = Modifier.align(Alignment.CenterVertically)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
          contentDescription = "more-info",
          tint = Color.White,
          modifier = Modifier.size(25.dp)
        )
      }
    }
  }
}


@Preview(showBackground = false)
@Composable
fun GreetingPreview2() {

  SettingsUser(
    "Sebastian Cruz",
    phone = "0978601625",
    email = "sebas.cruz750@gmail.com",
    rememberNavController()
  )

}
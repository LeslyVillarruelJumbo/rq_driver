package ec.edu.epn.rq_user.uin.profile

import android.os.Build

import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
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
fun UserHouseScreen(navController: NavController) {
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

    SetHome(navController)
  }
}


@Composable
fun SetHome(navController: NavController) {
  var inputHomeAddress by remember { mutableStateOf("") }

  val nearPlaces =
    listOf("Iglesia de Solanda", "Avenida Ajaví", "Mercado Mayorista", "Estadio de Solanda")
  Column(
    modifier = Modifier
      .padding(top = 55.dp)
      .padding(horizontal = 20.dp)
      .wrapContentHeight()
  ) {
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(80.dp))
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = Color(38, 70, 83))
    ) {
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .align(alignment = Alignment.Center)
      ) {
        Column(
          modifier = Modifier
            .align(Alignment.CenterVertically)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back-arrow",
            modifier = Modifier
              .size(24.dp)
              .clickable(onClick = { navController.navigate("configuracion") }),
            tint = Color.White
          )

        }
        Column(
          modifier = Modifier
            .align(Alignment.CenterVertically),
        ) {
          TextField(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .align(Alignment.CenterHorizontally)
              .height(60.dp),
            value = inputHomeAddress,
            onValueChange = { newText -> inputHomeAddress = newText },
            placeholder = {
              Text(
                fontSize = 22.sp,
                text = "I Arteta",
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
              )
            },
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedContainerColor = Color.Transparent,
              focusedContainerColor = Color.Transparent,
              focusedTextColor = Color.White,
              cursorColor = Color.Gray,
            ),
          )
        }
        Column(
          modifier = Modifier
            .align(Alignment.CenterVertically)
        ) {
          Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = "back-arrow",
            modifier = Modifier
              .size(24.dp),
            tint = Color.White
          )
        }
      }

    }
    // FINAL DE INPUT SEARCH

    Spacer(Modifier.height(15.dp))
    // iteramos las direcciones
    nearPlaces.forEach { place ->

      Row(
        modifier = Modifier
          .align(Alignment.Start)
          .padding(vertical = 20.dp)
      )
      {
        Icon(
          imageVector = Icons.Outlined.LocationOn,
          contentDescription = "address-icon",
          modifier = Modifier
            .size(20.dp)
            .align(Alignment.CenterVertically),
          tint = Color.White
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
          text = place,
          color = Color.White,
          fontSize = 16.sp
        )
      }
    }

    Row(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(vertical = 40.dp)
    )
    {
      Spacer(modifier = Modifier.width(20.dp))
      Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(38, 70, 83),
        )

      ) {

        Icon(
          imageVector = Icons.Outlined.Search,
          contentDescription = "address-icon",
          modifier = Modifier
            .size(20.dp)
            .align(Alignment.CenterVertically),
          tint = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = "Marca la dirección en el mapa",
          color = Color.White,
          fontSize = 16.sp
        )
      }
    }
  }
}

@Preview(showBackground = false)
@Composable
fun GreetingPreview7() {

  SetHome(rememberNavController())

}
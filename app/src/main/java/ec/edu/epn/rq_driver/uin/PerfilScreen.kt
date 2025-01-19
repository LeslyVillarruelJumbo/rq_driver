package ec.edu.epn.rq_driver.uin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import ec.edu.epn.rq_driver.R
import ec.edu.epn.rq_driver.model.Usuario
import ec.edu.epn.rq_driver.ui.theme.Rojo
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerfilScreen(navController: NavController, onLogout: () -> Unit = {}) {
    val scrollState = rememberScrollState()

    // Datos mockeados para el usuario
    val formatter = ofPattern("MM/dd/yyyy")
    val mockUser = Usuario(
        nombre = "Sebastian",
        apellido = "Cruz",
        email = "sebas.cruz750@gmail.com",
        telefono = "0978601625",
        cedula = "1719356006",
        fechaNacimiento = LocalDate.parse("04/11/2000", formatter),
        firesbaseId = "1"
    )

    // Interfaz de usuario
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(29, 53, 87))
            .verticalScroll(scrollState)
    ) {
        ProfileInfo(
            name = "${mockUser.nombre} ${mockUser.apellido}",
            navController = navController,
            onLogout = onLogout
        )
    }
}

@Composable
fun ProfileInfo(name: String, navController: NavController, onLogout: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de perfil
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .size(180.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.pfp),
            contentDescription = "Foto de perfil de $name"
        )

        // Nombre del usuario
        Text(
            text = name,
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(top = 24.dp)
        )

        // Botones
        Spacer(modifier = Modifier.height(30.dp))
        ProfileButtons(navController, onLogout)
    }
}

@Composable
fun ProfileButtons(navController: NavController, onLogout: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón de Configuración
        Column(
            modifier = Modifier
                .padding(8.dp)
                .height(90.dp)
                .width(350.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(38, 70, 83))
                .clickable { navController.navigate("configuracion") },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Configuración",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Configuración",
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }

        // Botón de Historial de Viajes
        Spacer(modifier = Modifier.height(10.dp))
        ProfileButton(
            icon = Icons.Default.DateRange,
            text = "Historial de viajes",
            onClick = { navController.navigate("historial_viajes") }
        )

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Rojo),
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(vertical = 30.dp)
        ) {
            Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(width = 350.dp, height = 90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(38, 70, 83))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(29, 53, 87))
    ) {
        ProfileInfo(name = "Sebastian Cruz", rememberNavController())
    }
}
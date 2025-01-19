package ec.edu.epn.rq_driver.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ec.edu.epn.rq_driver.NavBar
import ec.edu.epn.rq_driver.uin.ExploraScreen
import ec.edu.epn.rq_driver.uin.CreaRutaScreen
import ec.edu.epn.rq_driver.uin.FavoritasScreen
import ec.edu.epn.rq_driver.uin.PerfilScreen
import ec.edu.epn.rq_driver.uin.RecuperarCuentaScreen
import ec.edu.epn.rq_driver.uin.LogInScreen
import ec.edu.epn.rq_driver.uin.SignUpScreen
import ec.edu.epn.rq_user.uin.profile.UserEmailScreen
import ec.edu.epn.rq_user.uin.profile.UserHouseScreen
import ec.edu.epn.rq_user.uin.profile.UserInfoScreen
import ec.edu.epn.rq_user.uin.profile.UserNameScreen
import ec.edu.epn.rq_user.uin.profile.UserPhoneScreen
import ec.edu.epn.rq_user.uin.profile.UserSettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier, logged: MutableState<Boolean> = remember { mutableStateOf(false) }) {
    Scaffold(
        bottomBar = { if (logged.value) NavBar(navController) }  // ✅ Se asegura que el NavBar esté presente
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier
                .fillMaxSize()  // ✅ Asegurar que el NavHost use el espacio correctamente
                .padding(innerPadding)  // ✅ Evita que el contenido se superponga con el NavBar
        ) {
            // Pantallas de la barra de navegación
            composable("explora") { ExploraScreen(navController) }
            composable("crearuta") { CreaRutaScreen(navController) }
            composable("favoritas") { FavoritasScreen(navController) }
            composable("perfil") { PerfilScreen(navController) }

            // Pantallas Módulo LogIn/SignUp
            composable("login") { LogInScreen(navController, logged) }
            composable("signup") { SignUpScreen(navController) }
            composable("recuperar") { RecuperarCuentaScreen(navController) }

            // Pantallas de Profile
            composable("configuracion") { UserSettingsScreen(navController)}
            composable("informacion") { UserInfoScreen(navController) }
            composable("updateNombre") { UserNameScreen(navController) }
            composable("updateTelefono") { UserPhoneScreen(navController) }
            composable("updateEmail") { UserEmailScreen(navController) }
            composable("updateHouse") { UserHouseScreen(navController) }


        }
    }
}

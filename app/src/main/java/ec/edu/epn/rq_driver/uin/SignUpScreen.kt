package ec.edu.epn.rq_driver.uin

import ec.edu.epn.rq_driver.R
import ec.edu.epn.rq_driver.components.Avatar
import ec.edu.epn.rq_driver.components.Input
import ec.edu.epn.rq_driver.components.Utils
import ec.edu.epn.rq_driver.navigation.AppNavigation
import ec.edu.epn.rq_driver.ui.theme.Verde
import androidx.compose.foundation.gestures.scrollBy
import android.os.Handler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

//@Composable
//fun SignUpScreen(nav: NavHostController = rememberNavController()) {
//    var submitted by remember { mutableStateOf(false) }
//    val firstName = remember { mutableStateOf("") }
//    val lastName = remember { mutableStateOf("") }
//    val id = remember { mutableStateOf("") }
//    val celNum = remember { mutableStateOf("") }
//    val mail = remember { mutableStateOf("") }
//    val username = remember { mutableStateOf("") }
//    val password = remember { mutableStateOf("") }
//    val passwordConfirmation = remember { mutableStateOf("") }
//
//    val scrollState = rememberScrollState()
//    var columnHeight by remember { mutableIntStateOf(0) }
//    val textFieldPositions = remember { mutableMapOf<Int, Int>() }
//    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
//    var focusedInputIndex: Int? by remember { mutableStateOf(null) }
//
//    LaunchedEffect(keyboardHeight) {
//        focusedInputIndex?.let {
//            val yPosition: Int = textFieldPositions[it]!!
//            if (yPosition <= 0) {
//                scrollState.scrollTo(yPosition)
//            } else if (yPosition > (keyboardHeight - scrollState.value)) {
//                scrollState.scrollBy(keyboardHeight.toFloat())
//            }
//        }
//    }
//
//    val modificador = { inputIndex: Int ->
//        Modifier
//            .onGloballyPositioned {
//                textFieldPositions[inputIndex] = it.positionInRoot().y.dp.value.toInt()
//            }
//            .onFocusChanged {
//                if (it.isFocused) {
//                    focusedInputIndex = inputIndex
//                }
//            }
//    }
//
//    class Campo (
//        val placeholder : String,
//        val value : MutableState<String>,
//        val hiddeable : Boolean = false,
//        val modifier : (Int) -> Modifier = modificador,
//        val tooltip: String? = null
//    )
//
//    val campos = arrayOf (
//        Campo(placeholder = "Nombre(s) *", value = firstName),
//        Campo(placeholder = "Apellido(s) *", value = lastName),
//        Campo(placeholder = "Cédula de Identidad *", value = id, tooltip = stringResource(R.string.tooltip_cedula)),
//        Campo(placeholder = "Número de Teléfono *", value = celNum, tooltip = stringResource(R.string.tooltip_telefono)),
//        Campo(placeholder = "Correo Electrónico *", value = mail),
//        Campo(placeholder = "Nombre de Usuario *", value = username, tooltip = stringResource(R.string.tooltip_usuario)),
//        Campo(placeholder = "Contraseña *", value = password, hiddeable = true),
//        Campo(placeholder = "Confirmar Contraseña *", value = passwordConfirmation, hiddeable = true)
//    )
//
//    val condition = true
//    fun signUpHandler() {
//        if(condition) {
//            submitted = true
//            @Suppress("DEPRECATION")
//            Handler().postDelayed({
//                run {
//                    nav.navigate("login")
//                }
//            },1000)
//        }
//    }
//
//    Box(Utils.universalModifier) {
//
//        if (!submitted) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight(.94f)
//                    .fillMaxWidth(25 / 32f)
//                    .align(Alignment.Center)
//                    .verticalScroll(scrollState)
//                    .imePadding()
//                    .onGloballyPositioned { coordinates ->
//                        columnHeight = coordinates.size.height
//                    },
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Row(Modifier.height(175.dp)) {
//                    Avatar()
//                }
//                Spacer(Modifier.height(15.dp))
//                Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
//                    campos.mapIndexed { indice, campo ->
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Box {
//                                Input(
//                                    placeholder = campo.placeholder,
//                                    value = campo.value,
//                                    hiddeable = campo.hiddeable,
//                                    modifier = campo.modifier(indice)
//                                )
//                            }
//                            campo.tooltip?.let {
//                                InfoTooltipButton(Modifier.align(Alignment.CenterVertically), it)
//                            }
//                        }
//                    }
//                }
//                Spacer(Modifier.height(15.dp))
//                Button(
//                    onClick = { signUpHandler() },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Verde,
//                        disabledContainerColor = Color.LightGray
//                    ),
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = firstName.value.isNotEmpty() &&
//                            lastName.value.isNotEmpty() &&
//                            id.value.isNotEmpty() &&
//                            celNum.value.isNotEmpty() &&
//                            mail.value.isNotEmpty() &&
//                            username.value.isNotEmpty() &&
//                            password.value.isNotEmpty() &&
//                            passwordConfirmation.value.isNotEmpty()
//                ) { Text("Crear Cuenta") }
//            }
//            IconButton(onClick = { nav.navigateUp() }, Modifier.offset(10.dp)) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBackIosNew,
//                    contentDescription = "Ir atrás",
//                    tint = Color.White,
//                    modifier = Modifier.size(25.dp)
//                )
//            }
//        } else {
//
//            Column(
//                Modifier
//                    .align(Alignment.Center)
//                    .fillMaxSize(.75f),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Check,
//                    contentDescription = "Registro Exitoso",
//                    tint = Verde,
//                    modifier = Modifier
//                        .aspectRatio(1f)
//                        .fillMaxWidth()
//                )
//                Text(
//                    "¡Registro Exitoso!",
//                    color = Verde,
//                    fontWeight = FontWeight.Black,
//                    fontSize = 35.sp
//                )
//            }
//
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTooltipButton(modifier: Modifier, tooltip: String) {
    val tooltipPosition = TooltipDefaults.rememberPlainTooltipPositionProvider()
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    TooltipBox (
        tooltip = {
            Text (
                text = tooltip,
                color = Color(0xFF1D3557),
                modifier = modifier
                    .border(1.dp, Color(0xFF1D3557), RoundedCornerShape(15.dp))
                    .shadow(15.dp, RoundedCornerShape(15.dp))
                    .background(Color(0xFF95A0B0))
                    .fillMaxWidth(.6f)
                    .defaultMinSize(minHeight = 80.dp)
                    .padding(15.dp)
            )
        },
        positionProvider = tooltipPosition,
        state = tooltipState
    ) {
        IconButton(
            onClick = { scope.launch { tooltipState.show() } },
            modifier = Modifier
                .requiredSize(30.dp)
                .offset(15.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Más Información",
                tint = Color(0x77FFFFFF),
                modifier = Modifier.fillMaxSize(.9f)
            )
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = false,
    device = "spec:width=720px,height=1612px,dpi=320,navigation=buttons", name = "Tekno",
    apiLevel = 34
)
@Composable
private fun SignUpPreview() {
    val navController = rememberNavController()
//    AppNavigation(navController)
}
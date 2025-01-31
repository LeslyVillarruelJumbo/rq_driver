package ec.edu.epn.rq_driver.components

import ec.edu.epn.rq_driver.components.Utils.DefaultOverlay
import ec.edu.epn.rq_driver.ui.theme.Azul
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    placeholder: String = "",
    hiddeable: Boolean = false,
    value: MutableState<String>? = null,
    error: Boolean? = null,
    modifier: Modifier = Modifier
) {
    val internalValue = value ?: remember { mutableStateOf("") }
    val internalError = remember { mutableStateOf(error ?: false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val unfocusedContainerColor = Color(0xE895A0B0)
    val textFieldShape = Shapes().small

    BasicTextField(
        value = internalValue.value,
        onValueChange = { internalValue.value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (hiddeable && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        decorationBox = {
            TextFieldDefaults.DecorationBox(
                value = internalValue.value,
                placeholder = { Text(placeholder) },
                enabled = true,
                isError = internalError.value,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = unfocusedContainerColor,
                    unfocusedPlaceholderColor = Azul,
                    errorPlaceholderColor = Azul,
                    errorContainerColor = if(internalValue.value.isEmpty()) unfocusedContainerColor else Color.Unspecified,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                innerTextField = it,
                interactionSource = interactionSource,
                visualTransformation = if (hiddeable && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    if (hiddeable) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Azul
                            )
                        }
                    } else {
                        @Suppress("UNUSED_EXPRESSION")
                        null
                    }
                },
                contentPadding = PaddingValues(12.dp,8.dp),
            )
        },
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .clip(textFieldShape)
            .border(1.dp, if (internalError.value) Color.Red else Color.Unspecified, textFieldShape)
    )
}

@Preview (
    backgroundColor = 0xFF1D3556, showBackground = false, showSystemUi = true
)
@Composable
private fun InputPreview() {
    val value = remember { mutableStateOf("") }
    val error = remember { mutableStateOf(false) }

    DefaultOverlay {
        Input (
            placeholder = "Cédula de Identidad *",
            hiddeable = true,
            value = value,
            error = error.value
        )
        Row {
            Button(onClick = {error.value = !error.value}) {
                Text("ERROR")
            }
        }
        Text(value.value)
    }
}
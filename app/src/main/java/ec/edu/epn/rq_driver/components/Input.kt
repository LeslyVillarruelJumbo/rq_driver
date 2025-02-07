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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import ec.edu.epn.rq_driver.ui.theme.Gris
import ec.edu.epn.rq_driver.ui.theme.GrisOscuro


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    placeholder: String = "",
    value: MutableState<String>? = null,
    hiddeable: Boolean = false,
    error: Boolean? = null,
    enabled: Boolean = true,
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
        enabled = enabled,
        onValueChange = { internalValue.value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (hiddeable && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        decorationBox = {
            TextFieldDefaults.DecorationBox(
                value = internalValue.value,
                placeholder = { Text(placeholder) },
                enabled = enabled,
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(fieldValues: Campo, modifier: Modifier = Modifier) {

    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val visualTransformation = when {

        fieldValues.fieldOptions.hiddeable && !passwordVisible -> {
            PasswordVisualTransformation()
        }

        else -> { VisualTransformation.None }

    }
    val fieldLabel: @Composable (() -> Unit)? = fieldValues.label?.let {
        @Composable { Text(it, style = MaterialTheme.typography.bodyMedium) }
    }
    val trailingIcon: @Composable (() -> Unit)? = when {

        fieldValues.fieldOptions.hiddeable -> {
            @Composable {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Esconder contraseña" else "Mostrar contraseña",
                        tint = Azul
                    )
                }
            }
        }

        fieldValues.hasInteracted && fieldValues.errorState.status -> {
            @Composable {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Error en el campo",
                    tint = Azul
                )
            }
        }

        else -> { null }
    }
    val supportingText: @Composable (() -> Unit)? = fieldValues.errorState.message?.let {

        if (fieldValues.hasInteracted && fieldValues.errorState.status) {
            @Composable {
                Text(
                    text = it, modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        else { null}

    }
    val colors = TextFieldDefaults.colors(
        unfocusedContainerColor = Gris,
        unfocusedPlaceholderColor = Azul,
        errorPlaceholderColor = Azul,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = GrisOscuro
    )
    val textFieldShape = Shapes().small
    val textFieldModifier = modifier
        .height(40.dp)
        .fillMaxWidth()
        .clip(textFieldShape)
        .border(
            1.dp,
            if (fieldValues.errorState.status) Color.Red else Color.Unspecified,
            textFieldShape
        )
    val decorationBox:  @Composable (innerTextField: @Composable () -> Unit) -> Unit = @Composable {
        TextFieldDefaults.DecorationBox(
            value = fieldValues.value.value,
            enabled = fieldValues.enabled,
            isError = fieldValues.hasInteracted && fieldValues.errorState.status,
            singleLine = true,
            innerTextField = it,
            interactionSource = interactionSource,
            visualTransformation = visualTransformation,
            contentPadding = PaddingValues(12.dp,8.dp),
            placeholder = { fieldValues.placeholder?.let { placeholder -> Text(placeholder) } },
            label = fieldLabel,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            colors = colors
        )
    }


    BasicTextField(
        value = fieldValues.value.value,
        enabled = fieldValues.enabled,
        onValueChange = { fieldValues.value.value = it },
        keyboardOptions = fieldValues.fieldOptions.keyBoardOptions,
        keyboardActions = fieldValues.fieldOptions.keyBoardActions,
        visualTransformation = visualTransformation,
        singleLine = true,
        decorationBox = decorationBox,
        modifier = textFieldModifier
    )
}



@Preview (backgroundColor = 0xFF1D3556, showBackground = false, showSystemUi = true)
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

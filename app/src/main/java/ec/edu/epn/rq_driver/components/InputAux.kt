package ec.edu.epn.rq_driver.components

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


fun getModificador(
    firstRender: MutableMap<Int, Boolean>,
    textFieldPositions: MutableMap<Int, Int>,
    focusedInputIndex: MutableState<Int?>,
): (Int) -> Modifier {

    return { inputIndex: Int ->

        if (firstRender[inputIndex] == null) firstRender[inputIndex] = true

        Modifier
            .onGloballyPositioned {
                if (firstRender[inputIndex] == false) return@onGloballyPositioned
                textFieldPositions[inputIndex] = it.positionInWindow().y.dp.value.toInt()
                firstRender[inputIndex] = false
            }
            .onFocusChanged {
                if (firstRender[inputIndex] == true) return@onFocusChanged
                if (it.isFocused) {
                    focusedInputIndex.value = inputIndex
                }
            }

    }

}


sealed class FieldOptions(
    val hiddeable : Boolean = false,
    val keyBoardOptions: KeyboardOptions = KeyboardOptions.Default,
    val keyBoardActions: KeyboardActions = KeyboardActions.Default
) {
    class Default : FieldOptions()
    class RegularTextField(
        imeAction: ImeAction = ImeAction.Next,
        onDone: (KeyboardActionScope.() -> Unit)? = {}
    ) : FieldOptions(
        keyBoardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        keyBoardActions = KeyboardActions(onDone = onDone)
    )

    class NumericTextField(
        imeAction: ImeAction = ImeAction.Next,
        onDone: (KeyboardActionScope.() -> Unit)? = {}
    ) : FieldOptions(
        keyBoardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyBoardActions = KeyboardActions(onDone = onDone)
    )

    class PhoneTextField(
        imeAction: ImeAction = ImeAction.Next,
        onDone: (KeyboardActionScope.() -> Unit)? = {}
    ) : FieldOptions(
        keyBoardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = imeAction
        ),
        keyBoardActions = KeyboardActions(onDone = onDone)
    )

    class EmailTextField(
        imeAction: ImeAction = ImeAction.Next,
        onDone: (KeyboardActionScope.() -> Unit)? = {}
    ) : FieldOptions(
        keyBoardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyBoardActions = KeyboardActions(onDone = onDone)
    )

    class PasswordTextField(
        imeAction: ImeAction = ImeAction.Next,
        onDone: (KeyboardActionScope.() -> Unit)? = {}
    ) : FieldOptions(
        hiddeable = true,
        keyBoardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyBoardActions = KeyboardActions(onDone = onDone)
    )


}

class ErrorState (
    val status: Boolean = false,
    val message: String? = null
)

class Campo (
    val value : MutableState<String> = mutableStateOf(""),
    val label : String? = null,
    val placeholder : String? = null,
    val hasInteracted: Boolean = false,
    val errorState: ErrorState = ErrorState(),
    val tooltip: String? = null,
    val fieldOptions: FieldOptions = FieldOptions.Default(),
    val hidden: Boolean = false,
    val enabled: Boolean = true
)

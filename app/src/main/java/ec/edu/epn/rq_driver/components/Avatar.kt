package ec.edu.epn.rq_driver.components

import ec.edu.epn.rq_driver.R
import ec.edu.epn.rq_driver.ui.theme.Azul
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun Avatar() {
    var imagen : Int? by rememberSaveable { mutableStateOf(null) }
    var currentHeight by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->
                currentHeight = coordinates.size.height.toFloat()
            }
    ) {
        Image(
            painter = painterResource(imagen ?: R.drawable.default_user),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(if(imagen == null) Color(0xE895A0B0) else Color.Unspecified)
                .border(1.dp, Azul, CircleShape)
        )
        IconButton(
            onClick = { imagen = R.drawable.pfp },
            modifier = Modifier
                .fillMaxSize(1 / 5f)
                .align(Alignment.BottomEnd)
                .offset(
                    (-currentHeight / 75).dp,
                    (-currentHeight / 75).dp
                )
                .clip(CircleShape)
                .background(Color(0xE895A0B0))
                .border(1.dp, Azul, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "",
                tint = Azul,
                modifier = Modifier.fillMaxSize(.8f)
            )
        }
    }
}

@Preview
@Composable
fun AvatarPreview() {
    Avatar()
}
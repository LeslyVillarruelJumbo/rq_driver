package ec.edu.epn.rq_driver.components

import ec.edu.epn.rq_driver.R
import ec.edu.epn.rq_driver.ui.theme.Azul
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import ec.edu.epn.rq_driver.ui.theme.Gris
import ec.edu.epn.rq_driver.ui.theme.Rojo


@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun Avatar(imageUri: Uri? = null, modifier: Modifier = Modifier) {

    var imgUri: Uri? by rememberSaveable { mutableStateOf(null) }
    var painter: Painter? by remember { mutableStateOf(null) }
    var currentHeight by remember { mutableFloatStateOf(0f) }

    val asyncPainter = rememberAsyncImagePainter(imgUri)

    val testPicture = painterResource(R.drawable.pfp)
    val imagenPorDefecto = painterResource(R.drawable.default_user)


    LaunchedEffect(imageUri) { imgUri = imageUri }
    LaunchedEffect(imgUri) { painter = if (imgUri != null) asyncPainter else null }
    LaunchedEffect(asyncPainter.state) { if (asyncPainter.state is AsyncImagePainter.State.Error) imgUri = null }

    val removePicture = { imgUri = null }
//    val addPicture = { removePicture().also { painter = testPicture } }
    val addPicture = { removePicture().also { imgUri = Uri.parse("https://i.pinimg.com/736x/6d/c3/f3/6dc3f398a9e4c27b2d197e424ec6ecf7.jpg") } }
//    val addPicture = { removePicture().also { imgUri = Uri.parse("https://www.colejohnsonwrites.com/content/images/2024/10/David-Martinez-Cyberpunk-Edgerunners-1.jpg") } }
//    val addPicture = { removePicture().also { imgUri = Uri.parse("https://i.pinimg.com/736x/6c/1d/5a/6c1d5a989a84900ae0c488b9e0c504f1.jpg") } }
//    val addPicture = { removePicture().also { imgUri = Uri.parse("https://gear.cdprojektred.com/cdn/shop/files/600x900_DAVIDPLUSH1.jpg?v=1707895215") } }


    Box(
        modifier = modifier
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->
                currentHeight = coordinates.size.height.toFloat()
            }
    ) {

        ImagenDePerfil(
            painter = painter,
            imagenPorDefecto = imagenPorDefecto
        )

        AnimacionDeCarga(
            estadoDeCargaDeImagen = asyncPainter.state,
            modificadorDeAlineacion = Modifier.align(Alignment.Center)
        )

        AvatarIconButton(
            onClick = addPicture,
            enabled = asyncPainter.state !is AsyncImagePainter.State.Loading,
            iconSize = .8f,
            modifier = Modifier.align(Alignment.BottomEnd),
            icon = if (painter == null) Icons.Outlined.AddPhotoAlternate else Icons.Outlined.Edit
        )

        painter?.let {

            AvatarIconButton(
                onClick = removePicture,
                icon = Icons.Outlined.Clear,
                color = Rojo,
                modifier = Modifier
                    .scale(1 / 2f)
                    .align(Alignment.TopEnd)
                    .offset((currentHeight / 4).dp)
            )

        }

    }

}

@Composable
fun AnimacionDeCarga(
    modificadorDeAlineacion: Modifier = Modifier,
    estadoDeCargaDeImagen: AsyncImagePainter.State = AsyncImagePainter.State.Empty
) {
    if (estadoDeCargaDeImagen is AsyncImagePainter.State.Loading) {
        CircularProgressIndicator(
            color = Gris,
            strokeWidth = 3.dp,
            modifier = Modifier
                .fillMaxSize()
                .scale(.5f)
                .then(modificadorDeAlineacion)
        )
    }
}

@Composable
fun ImagenDePerfil(
    painter: Painter? = null,
    imagenPorDefecto: Painter = painterResource(R.drawable.default_user)
) {
    Image(
        painter = painter ?: imagenPorDefecto,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(if (painter == null) Gris else Color.Unspecified)
            .border(3.dp, Gris, CircleShape)
    )
}

@Composable
fun AvatarIconButton(
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    iconSize: Float = .9f,
    color: Color = Gris,
    icon: ImageVector = Icons.Outlined.DisabledByDefault
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Azul, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = Azul,
            modifier = Modifier.fillMaxSize(iconSize)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun AvatarPreview() {
    Avatar()
}
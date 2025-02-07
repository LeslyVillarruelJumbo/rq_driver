package ec.edu.epn.rq_driver.components

import android.graphics.BlurMaskFilter
import android.util.Log
import ec.edu.epn.rq_driver.ui.theme.Azul
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlinx.coroutines.launch
import java.util.Date
import java.util.GregorianCalendar

object Utils {

    var universalModifier : Modifier = Modifier.background(Azul).fillMaxSize()

    fun Modifier.animatePlacement(
        animationSpec: AnimationSpec<IntOffset> = spring(stiffness = Spring.StiffnessMediumLow)
    ): Modifier = composed {
        val scope = rememberCoroutineScope()
        var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
        var animatable by remember {
            mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
        }
        // 🔥 onPlaced should be before offset Modifier
        this.onPlaced {
            // Calculate the position in the parent layout
            targetOffset = it.positionInParent().round()
        }.offset {
            // Animate to the new target offset when alignment changes.
            val anim = animatable ?: Animatable(targetOffset, IntOffset.VectorConverter)
                .also {
                    animatable = it
                }

            if (anim.targetValue != targetOffset) {
                scope.launch {
                    anim.animateTo(targetOffset, animationSpec)
                }
            }
            // Offset the child in the opposite direction to the targetOffset, and slowly catch
            // up to zero offset via an animation to achieve an overall animated movement.
            animatable?.let { it.value - targetOffset } ?: IntOffset.Zero
        }
    }

    fun Modifier.dropShadow(
        shape: RoundedCornerShape = CircleShape,
        color: Color = Color.Black.copy(0.25f),
        blur: Dp = 0.dp,
        offsetY: Dp = 0.dp,
        offsetX: Dp = 0.dp,
        spread: Dp = 0.dp
    ) = this.drawBehind {

        val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
        val paint = Paint()
        paint.color = color

        if (blur.toPx() > 0) {
            paint.asFrameworkPaint().apply {
                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetX.toPx(), offsetY.toPx())
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }

    @Composable
    fun DefaultOverlay(child: @Composable () -> Unit = { Text("Default Overlay") }) {
        Surface(Modifier.fillMaxSize()) {
            Box(universalModifier) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(13 / 16f)
                        .align(Alignment.Center)
                ) { child() }
            }
        }
    }

}


fun getDateObject(year: Int, month: Int, dayOfMonth: Int) : Date {
    return try {
        GregorianCalendar(year,month-1,dayOfMonth).time
    } catch (e: Exception) {
        Log.e("Utils", "Error al interpretar la fecha proporcionada (${e.message})")
        Date()
    }
}
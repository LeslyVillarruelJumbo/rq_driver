package ec.edu.epn.rq_driver.components

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
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import kotlinx.coroutines.launch

object Utils {

    var universalModifier : Modifier = Modifier
        .background(Azul)
        .fillMaxSize()
        .systemBarsPadding()

    fun Modifier.animatePlacement(
        animationSpec: AnimationSpec<IntOffset> = spring(stiffness = Spring.StiffnessMediumLow)
    ): Modifier = composed {
        val scope = rememberCoroutineScope()
        var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
        var animatable by remember {
            mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
        }
        this
            // 🔥 onPlaced should be before offset Modifier
            .onPlaced {
                // Calculate the position in the parent layout
                targetOffset = it
                    .positionInParent()
                    .round()
            }
            .offset {
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
                ) {
                    child()
                }
            }
        }
    }

}
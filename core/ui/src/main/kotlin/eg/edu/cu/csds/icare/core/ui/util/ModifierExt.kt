package eg.edu.cu.csds.icare.core.ui.util

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.animatedBorder(
    borderColors: List<Color>,
    backgroundColor: Color,
    shape: Shape = RectangleShape,
    borderWidth: Dp = 1.dp,
    animationDurationInMillis: Int = 1500,
    easing: Easing = LinearEasing,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = animationDurationInMillis, easing = easing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "",
    )
    val brush = Brush.sweepGradient(borderColors)

    return this
        .clip(shape) // Apply the specified shape to the border.
        .padding(borderWidth)
        .drawWithContent {
            rotate(angle) {
                drawCircle(
                    brush = brush,
                    radius = size.width,
                    blendMode = BlendMode.SrcIn,
                )
            }
            drawContent()
        }.background(color = backgroundColor, shape = shape)
}

@Composable
fun Modifier.neumorphicUp(
    shape: Shape,
    shadowPadding: Dp,
): Modifier =
    background(
        color = MaterialTheme.colorScheme.background,
        shape = shape,
    ).innerShadow(
        shape = shape,
        shadow =
            Shadow(
                radius = shadowPadding,
                color = MaterialTheme.colorScheme.surfaceBright,
                offset = DpOffset(x = shadowPadding, y = shadowPadding),
            ),
    ).innerShadow(
        shape = shape,
        shadow =
            Shadow(
                radius = shadowPadding,
                color = MaterialTheme.colorScheme.surfaceDim,
                offset = DpOffset(x = -shadowPadding, y = -shadowPadding),
            ),
    )

@Composable
fun Modifier.neumorphicDown(
    shape: Shape,
    shadowPadding: Dp,
): Modifier =
    background(
        color = MaterialTheme.colorScheme.background,
        shape = shape,
    ).innerShadow(
        shape = shape,
        shadow =
            Shadow(
                radius = shadowPadding,
                color = MaterialTheme.colorScheme.surfaceBright,
                offset = DpOffset(x = -shadowPadding, y = -shadowPadding),
            ),
    ).innerShadow(
        shape = shape,
        shadow =
            Shadow(
                radius = shadowPadding,
                color = MaterialTheme.colorScheme.surfaceDim,
                offset = DpOffset(x = shadowPadding, y = shadowPadding),
            ),
    )

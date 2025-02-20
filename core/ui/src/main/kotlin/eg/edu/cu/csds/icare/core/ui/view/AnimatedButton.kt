package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.THICK_BORDER_STROKE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import kotlinx.coroutines.launch

@Composable
fun AnimatedButton(
    modifier: Modifier = Modifier,
    animationDuration: Int = 100,
    scaleDown: Float = 0.9f,
    enabled: Boolean = true,
    fontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    borderColor: Color = Yellow500,
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scale =
        remember {
            Animatable(1f)
        }
    OutlinedButton(
        modifier =
            modifier
                .scale(scale = scale.value),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = color),
        shape = RoundedCornerShape(topEnd = S_PADDING, bottomStart = S_PADDING),
        border = BorderStroke(THICK_BORDER_STROKE_SIZE, borderColor),
        onClick = {
            coroutineScope.launch {
                scale.animateTo(
                    scaleDown,
                    animationSpec = tween(animationDuration),
                )
                scale.animateTo(
                    1f,
                    animationSpec = tween(animationDuration),
                )
            }
            onClick()
        },
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = helveticaFamily,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

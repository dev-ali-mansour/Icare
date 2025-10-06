package eg.edu.cu.csds.icare.splash

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.core.ui.theme.SPLASH_LOGO_SIZE
import eg.edu.cu.csds.icare.core.ui.R.drawable

@Composable
internal fun SplashScreen() {
    val degrees = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        degrees.animateTo(
            targetValue = 360f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            durationMillis = 1000,
                            delayMillis = 750,
                        ),
                    repeatMode = RepeatMode.Restart,
                ),
        )
    }

    Splash(degrees = degrees.value)
}

@Composable
private fun Splash(degrees: Float) {
    val brush =
        if (isSystemInDarkTheme()) {
            Brush.verticalGradient(
                listOf(Color.Black, Color.Black.copy(alpha = 0.6f)),
            )
        } else {
            Brush.verticalGradient(
                listOf(Color.White, Color.White.copy(alpha = 0.6f)),
            )
        }
    Box(
        modifier =
            Modifier
                .background(brush)
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .size(SPLASH_LOGO_SIZE)
                    .rotate(degrees = degrees),
            painter = painterResource(id = drawable.core_ui_logo),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SplashScreenPreview() {
    Splash(degrees = 0f)
}

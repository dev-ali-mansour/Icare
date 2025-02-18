package eg.edu.cu.csds.icare

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.SPLASH_LOGO_SIZE

@Composable
internal fun SplashScreen(
    firebaseAuth: FirebaseAuth,
    mainViewModel: MainViewModel,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    val degrees = remember { Animatable(0f) }
    var isLoading by remember { mutableStateOf(true) }
    val resultResource by mainViewModel.resultFlow.collectAsStateWithLifecycle()
    val userResource by mainViewModel.currentUserFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        degrees.animateTo(
            targetValue = 360f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            durationMillis = 1000,
                            delayMillis = 20,
                        ),
                    repeatMode = RepeatMode.Restart,
                ),
        )
    }

    firebaseAuth.currentUser?.let {
        when (resultResource) {
            is Resource.Unspecified, is Resource.Loading -> isLoading = true

            is Resource.Success ->
                userResource.data?.let {
                    LaunchedEffect(key1 = Unit) {
                        navigateToHome()
                    }
                }

            is Resource.Error ->
                LaunchedEffect(key1 = true) {
                    onError(resultResource.error)
                }
        }
    } ?: run {
        LaunchedEffect(key1 = Unit) {
            navigateToLogin()
        }
    }
    if (isLoading) Splash(degrees = degrees.value)
}

@Composable
internal fun Splash(degrees: Float) {
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
            painter = painterResource(id = R.drawable.logo),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun SplashScreenPreview() {
    Splash(degrees = 0f)
}

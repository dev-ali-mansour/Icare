package eg.edu.cu.csds.icare.core.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <EFFECT> LaunchedUiEffectHandler(
    effectFlow: Flow<EFFECT?>,
    onEffect: suspend (EFFECT) -> Unit,
    onConsumeEffect: () -> Unit,
) {
    val effect by effectFlow.collectAsStateWithLifecycle(null)
    val currentOnEffect by rememberUpdatedState(onEffect)
    val currentOnConsumeEffect by rememberUpdatedState(onConsumeEffect)

    LaunchedEffect(effect) {
        effect?.let {
            currentOnEffect(it)
            currentOnConsumeEffect()
        }
    }
}

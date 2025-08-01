package eg.edu.cu.csds.icare.auth.screen.signin

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.auth.screen.SignInAction
import eg.edu.cu.csds.icare.auth.screen.SignInViewModel
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.signInWithGoogle
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onRecoveryClicked: () -> Unit,
    onCreateAnAccountClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    context: Context = LocalContext.current,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(StartActivityForResult()) {
            viewModel.onAction(SignInAction.UpdateGoogleSignInIntent(it.data))
            viewModel.onAction(SignInAction.SignInWithGoogle)
        }

    LaunchedEffect(state.signInSuccess) {
        if (state.signInSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            SignInContent(
                state = state,
                onAction = { action ->
                    when (action) {
                        is SignInAction.SignInWithGoogle ->
                            context.signInWithGoogle(launcher = launcher)

                        is SignInAction.ResetPassword -> {
                            onRecoveryClicked()
                        }

                        is SignInAction.CreateAccount -> {
                            onCreateAnAccountClicked()
                        }

                        else -> {
                            viewModel.onAction(action = action)
                        }
                    }
                },
                onError = {
                    alertMessage = it
                    scope.launch {
                        showAlert = true
                        delay(timeMillis = 3000)
                        showAlert = false
                    }
                },
            )

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

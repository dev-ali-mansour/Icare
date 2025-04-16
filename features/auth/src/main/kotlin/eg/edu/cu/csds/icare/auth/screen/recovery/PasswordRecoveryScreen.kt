package eg.edu.cu.csds.icare.auth.screen.recovery

import android.content.Context
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
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun PasswordRecoveryScreen(
    onLoginClicked: () -> Unit,
    onRecoveryCompleted: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    authViewModel: AuthViewModel,
    context: Context = LocalContext.current,
) {
    var email by authViewModel.emailState
    val isLoading by authViewModel.isLoading
    val resource by authViewModel.recoveryResFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        LaunchedEffect(key1 = resource) {
            if (resource is Resource.Success) {
                alertMessage = context.getString(R.string.recovery_email_sent)
                showAlert = true
                delay(timeMillis = 3000)
                showAlert = false
                onRecoveryCompleted()
            } else if (resource is Resource.Error) {
                onError(resource.error)
            }
        }

        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            PasswordRecoveryContent(
                email = email,
                isLoading = isLoading,
                onEmailChanged = { email = it },
                onResetButtonClicked = {
                    when {
                        !email.isValidEmail ->
                            scope.launch {
                                onError(Throwable(context.getString(R.string.email_error)))
                            }

                        else -> authViewModel.onResetPasswordClicked()
                    }
                },
                onLoginClicked = {
                    onLoginClicked()
                },
            )

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

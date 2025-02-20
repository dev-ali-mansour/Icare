package eg.edu.cu.csds.icare.auth.screen.login

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
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun LoginScreen(
    authViewModel: AuthViewModel,
    onRecoveryClicked: () -> Unit,
    onCreateAnAccountClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    context: Context = LocalContext.current,
) {
    val email by authViewModel.emailState
    val password by authViewModel.passwordState
    var passwordVisibility by authViewModel.passwordVisibility
    val isLoading by authViewModel.isLoading
    val loginRes by authViewModel.loginResFlow.collectAsStateWithLifecycle()
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

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
            LoginContent(
                email = email,
                password = password,
                passwordVisibility = passwordVisibility,
                isLoading = isLoading,
                onEmailChanged = { authViewModel.onEmailChanged(it) },
                onPasswordChanged = { authViewModel.onPasswordChanged(it) },
                onPasswordVisibilityChanged = { passwordVisibility = !passwordVisibility },
                onRecoveryClicked = { onRecoveryClicked() },
                onLoginButtonClicked = {
                    scope.launch {
                        when {
                            !email.isValidEmail -> {
                                alertMessage = context.getString(R.string.email_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            password.isBlank() -> {
                                alertMessage = context.getString(R.string.empty_password_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            else -> authViewModel.onLogInClicked()
                        }
                    }
                },
                onCreateAnAccountClicked = { onCreateAnAccountClicked() },
            )

            LaunchedEffect(key1 = loginRes) {
                when (loginRes) {
                    is Resource.Success -> {
                        onLoginSuccess()
                    }

                    is Resource.Error -> {
                        scope.launch {
                            alertMessage = context.getErrorMessage(loginRes.error)
                            showAlert = true
                            delay(timeMillis = 3000)
                            showAlert = false
                        }
                    }

                    else -> {}
                }
            }

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

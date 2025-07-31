package eg.edu.cu.csds.icare.auth.screen.profile

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.data.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.system.exitProcess
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun ProfileScreen(
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onError: suspend (Throwable?) -> Unit,
) {
    val logoutRes by authViewModel.logoutResFlow.collectAsStateWithLifecycle()
    val userResource by mainViewModel.currentUserFlow.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading by authViewModel.isLoading
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            authViewModel.linkGoogleAccount(it.data)
        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .padding(it),
        ) {
            ProfileContent(
                userResource = userResource,
                logoutRes = logoutRes,
                isLoading = isLoading,
                onGoogleClicked = { linkedWithGoogle ->
                    if (linkedWithGoogle) {
                        unLinkGoogleAccount(scope) {
                            mainViewModel.getUserInfo()
                        }
                    } else {
                        onGoogleClicked(context, launcher)
                    }
                },
                onLogoutClicked = { authViewModel.onLogOutClick() },
                onLogoutSucceed = {
                    scope.launch {
                        delay(timeMillis = 100)
                        exitProcess(0)
                    }
                },
                onError = { error -> onError(error) },
            )

            HandleLinkResult(
                snackBarHostState = snackBarHostState,
                viewModel = authViewModel,
                context = context,
                scope = scope,
            ) { mainViewModel.getUserInfo() }
        }
    }
}

@Composable
private fun HandleLinkResult(
    snackBarHostState: SnackbarHostState,
    viewModel: AuthViewModel,
    context: Context = LocalContext.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    onSuccess: () -> Unit,
) {
    val resource by viewModel.linkResFlow.collectAsState()

    LaunchedEffect(key1 = resource) {
        if (resource is Resource.Success) {
            onSuccess()
        } else if (resource is Resource.Error) {
            val message =
                when (resource.error) {
                    is FirebaseNetworkException -> context.getString(CoreR.string.network_error)
                    else -> {
                        Timber.e("Account Link Error: ${resource.error?.message}")
                        context.getString(CoreR.string.account_link_error)
                    }
                }
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long,
                )
            }
        }
    }
}

private fun onGoogleClicked(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {
    val gso =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInClient.revokeAccess()
    launcher.launch(googleSignInClient.signInIntent)
}

private fun unLinkGoogleAccount(
    scope: CoroutineScope,
    onSuccess: () -> Unit,
) {
    Firebase.auth.currentUser?.let { currentUser ->
        currentUser
            .unlink(GoogleAuthProvider.PROVIDER_ID)
            .addOnSuccessListener {
                scope.launch {
                    val profileUpdates =
                        UserProfileChangeRequest
                            .Builder()
                            .setPhotoUri(null)
                            .build()
                    currentUser
                        .updateProfile(profileUpdates)
                        .addOnSuccessListener { onSuccess() }
                }
            }
    }
}

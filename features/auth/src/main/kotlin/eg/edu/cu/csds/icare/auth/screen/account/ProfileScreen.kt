package eg.edu.cu.csds.icare.auth.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.getBasicInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
internal fun ProfileScreen(
    firebaseAuth: FirebaseAuth,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onError: suspend (Throwable?) -> Unit,
) {
    val logoutRes by authViewModel.logoutResFlow.collectAsStateWithLifecycle()
    val userResource by mainViewModel.currentUserFlow.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading
    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .padding(it),
        ) {
            ProfileContent(
                userResource = userResource,
                logoutRes = logoutRes,
                user = firebaseAuth.currentUser.getBasicInfo,
                isLoading = isLoading,
                onLogoutClicked = { authViewModel.onLogOutClick() },
                onLogoutSucceed = {
                    scope.launch {
                        delay(timeMillis = 100)
                        exitProcess(0)
                    }
                },
                onError = { error -> onError(error) },
            )
        }
    }
}

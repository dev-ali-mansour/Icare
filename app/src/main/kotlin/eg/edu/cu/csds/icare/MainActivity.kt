package eg.edu.cu.csds.icare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    private val firebaseAuth: FirebaseAuth by inject()
    private val mediaHelper: MediaHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.onBoardingCompleted.value !is Resource.Success
            }
        }
        enableEdgeToEdge()
        setContent {
            IcareTheme {
                MainScreen(
                    firebaseAuth = firebaseAuth,
                    mainViewModel = mainViewModel,
                    mediaHelper = mediaHelper,
                    showAppSettings = {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", packageName, null)
                            }
                        startActivity(intent)
                    },
                )
            }
        }
    }
}

package eg.edu.cu.csds.icare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.util.isInternetAvailable
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val onBoardingViewModel: OnBoardingViewModel by viewModel()
    private val appUpdateManager: AppUpdateManager by inject()
    private val updateType = AppUpdateType.IMMEDIATE
    private val updateFlowResultLauncher =
        (this as ComponentActivity)
            .registerForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                if (result.resultCode != RESULT_OK) {
                    Timber.e("Update flow failed! Result code: ${result.resultCode}")
                    exitProcess(0)
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isLoadingSplash by mutableStateOf(true)
        lifecycleScope.launch {
            onBoardingViewModel.uiState
                .onEach {
                    isLoadingSplash = it.isLoading
                }.launchIn(this)
        }
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isLoadingSplash
            }
        }

        enableEdgeToEdge()
        checkForUpdates()

        setContent {
            IcareTheme {
                MainScreen(onBoardingViewModel = onBoardingViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateFlowResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    )
                }
            }
    }

    private fun checkForUpdates() {
        if (!isInternetAvailable()) {
            Toast
                .makeText(
                    applicationContext,
                    getString(R.string.internet_connection_error),
                    Toast.LENGTH_LONG,
                ).show()
        }
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { info ->
                val isUpdateAvailable =
                    info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                if (isUpdateAvailable && info.isFlexibleUpdateAllowed) {
                    val starter =
                        IntentSenderForResultStarter {
                            intent,
                            _,
                            fillInIntent,
                            flagsMask,
                            flagsValues,
                            _,
                            _,
                            ->
                            val request =
                                IntentSenderRequest
                                    .Builder(intent)
                                    .setFillInIntent(fillInIntent)
                                    .setFlags(flagsValues, flagsMask)
                                    .build()
                            updateFlowResultLauncher.launch(request)
                        }
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        starter,
                        AppUpdateOptions
                            .newBuilder(updateType)
                            .setAllowAssetPackDeletion(true)
                            .build(),
                        MY_REQUEST_CODE,
                    )
                }
            }.addOnFailureListener {
                Timber.e("Update failed: ${it.message}")
            }
    }

    private companion object {
        private const val MY_REQUEST_CODE = 123
    }
}

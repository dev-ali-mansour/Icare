package eg.edu.cu.csds.icare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.util.isInternetAvailable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    private val mediaHelper: MediaHelper by inject()
    private val appUpdateManager: AppUpdateManager by inject()
    private val updateType = AppUpdateType.IMMEDIATE
    private val updateFlowResultLauncher =
        (this as ComponentActivity)
            .registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode != RESULT_OK) {
                    Timber.e("Update flow failed! Result code: ${result.resultCode}")
                    exitProcess(0)
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.onBoardingCompleted.value !is Resource.Success
            }
        }
        enableEdgeToEdge()
        checkForUpdates()
        setContent {
            IcareTheme {
                MainScreen(
                    mainViewModel = mainViewModel,
                    mediaHelper = mediaHelper,
                )
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
            lifecycleScope.launch {
                Toast
                    .makeText(
                        applicationContext,
                        getString(R.string.internet_connection_error),
                        Toast.LENGTH_LONG,
                    ).show()
                delay(3.milliseconds)
                exitProcess(0)
            }
        }
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { info ->
                val isUpdateAvailable =
                    info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                if (isUpdateAvailable && info.isFlexibleUpdateAllowed) {
                    val starter =
                        IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
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

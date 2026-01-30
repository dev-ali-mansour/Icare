package eg.edu.cu.csds.icare.feature.admin.screen.doctor.update

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.ui.common.CommonTopAppBar
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import eg.edu.cu.csds.icare.feature.admin.R
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorDetailsContent
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorEffect
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorIntent
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun UpdateDoctorScreen(
    onNavigationIconClicked: () -> Unit,
    onSuccess: () -> Unit,
) {
    val viewModel: UpdateDoctorViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(DoctorIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                DoctorEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    onSuccess()
                }

                is DoctorEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        },
    )

    Scaffold(
        topBar = {
            CommonTopAppBar(title = stringResource(id = R.string.feature_admin_update_doctor)) {
                onNavigationIconClicked()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        DoctorDetailsContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            uiState = uiState,
            onIntent = viewModel::handleIntent,
        )

        if (showSuccessDialog) SuccessesDialog {}
    }
}

package eg.edu.cu.csds.icare.feature.consultation.screen.add

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.ui.common.CommonTopAppBar
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import eg.edu.cu.csds.icare.feature.consultation.R
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationDetailsContent
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationEffect
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationIntent
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun NewConsultationScreen(
    onNavigationIconClicked: () -> Unit,
    navigateToMedicalRecord: (String) -> Unit,
) {
    val viewModel: NewConsultationViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(ConsultationIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is ConsultationEffect.NavigateToMedicalRecord -> {
                    navigateToMedicalRecord(effect.patientId)
                }

                is ConsultationEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    onNavigationIconClicked()
                }

                is ConsultationEffect.ShowError -> {
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
            CommonTopAppBar(title = stringResource(id = R.string.feature_consultations_new_consultation)) {
                onNavigationIconClicked()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.handleIntent(ConsultationIntent.Refresh)
                        },
                    ).padding(innerPadding),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (refresh, content) = createRefs()

                ConsultationDetailsContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    uiState = uiState,
                    onIntent = viewModel::handleIntent,
                )

                Indicator(
                    modifier =
                        Modifier.constrainAs(refresh) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    isRefreshing = uiState.isLoading,
                    state = refreshState,
                )

                if (showSuccessDialog) SuccessesDialog {}
            }
        }
    }
}

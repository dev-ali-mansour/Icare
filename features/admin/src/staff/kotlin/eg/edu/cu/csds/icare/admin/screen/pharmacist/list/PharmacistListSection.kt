package eg.edu.cu.csds.icare.admin.screen.pharmacist.list

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacistView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacistListSection(
    modifier: Modifier = Modifier,
    viewModel: PharmacistListViewModel = koinViewModel(),
    navigateToPharmacistDetails: (Pharmacist) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(PharmacistListEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is PharmacistListEffect.NavigateToPharmacistDetails -> {
                    navigateToPharmacistDetails(effect.pharmacist)
                }

                is PharmacistListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is PharmacistListEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        },
    )

    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .pullToRefresh(
                    state = refreshState,
                    isRefreshing = uiState.isLoading,
                    onRefresh = {
                        viewModel.processEvent(PharmacistListEvent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        PharmacistListContent(
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
            onEvent = viewModel::processEvent,
        )

        Indicator(
            modifier =
                Modifier
                    .constrainAs(refresh) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            isRefreshing = uiState.isLoading,
            state = refreshState,
        )

        if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
    }
}

@Composable
private fun PharmacistListContent(
    modifier: Modifier,
    uiState: PharmacistListState,
    onEvent: (PharmacistListEvent) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        val listState = rememberLazyListState()
        val expandedFabState = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onEvent(PharmacistListEvent.UpdateFabExpanded(expandedFabState.value))
        }
        if (uiState.pharmacists.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(CoreR.string.core_ui_no_pharmacies_data),
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    items = uiState.pharmacists,
                    key = { pharmacy ->
                        pharmacy.id
                    },
                ) { pharmacist ->
                    PharmacistView(pharmacist = pharmacist) {
                        onEvent(PharmacistListEvent.SelectPharmacist(pharmacist))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun PharmacistListSectionPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        PharmacistListContent(
            modifier = Modifier,
            uiState = PharmacistListState(),
            onEvent = {},
        )
    }
}

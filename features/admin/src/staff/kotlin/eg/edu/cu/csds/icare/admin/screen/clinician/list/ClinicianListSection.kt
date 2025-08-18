package eg.edu.cu.csds.icare.admin.screen.clinician.list

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.ClinicStaffView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import org.koin.androidx.compose.koinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianListSection(
    modifier: Modifier = Modifier,
    viewModel: ClinicianListViewModel = koinViewModel(),
    navigateToClinicianDetails: (Clinician) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: (String) -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.singleEvent.collect { event ->
            when (event) {
                is ClinicianListSingleEvent.NavigateToClinicianDetails -> {
                    navigateToClinicianDetails(event.clinician)
                }

                is ClinicianListSingleEvent.UpdateFabExpanded -> {
                    onExpandStateChanged(event.isExpanded)
                }

                is ClinicianListSingleEvent.ShowError -> {
                    onError(event.message.asString(context))
                }
            }
        }
    }

    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .pullToRefresh(
                    state = refreshState,
                    isRefreshing = state.isLoading,
                    onRefresh = {
                        viewModel.processIntent(ClinicianListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        ClinicianListContent(
            modifier =
                Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            state = state,
            onIntent = viewModel::processIntent,
        )

        Indicator(
            modifier =
                Modifier
                    .constrainAs(refresh) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            isRefreshing = state.isLoading,
            state = refreshState,
        )
    }
}

@Composable
private fun ClinicianListContent(
    modifier: Modifier,
    state: ClinicianListState,
    onIntent: (ClinicianListIntent) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        val listState = rememberLazyListState()
        val expandedFabState = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(ClinicianListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (state.clinicians.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(CoreR.string.no_clinic_staff_data),
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    items = state.clinicians,
                    key = { clinician ->
                        clinician.id
                    },
                ) { clinician ->
                    ClinicStaffView(clinician = clinician) {
                        onIntent(ClinicianListIntent.SelectClinician(clinician))
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
internal fun ClinicStaffsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        ClinicianListContent(
            modifier = Modifier,
            state = ClinicianListState(),
            onIntent = {},
        )
    }
}

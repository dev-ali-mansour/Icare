package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

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
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacyView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyListSection(
    modifier: Modifier = Modifier,
    viewModel: PharmacyListViewModel = koinViewModel(),
    navigateToPharmacyDetails: (Pharmacy) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.singleEvent.collect { event ->
            when (event) {
                is PharmacyListSingleEvent
                    .NavigateToPharmacyDetails,
                -> {
                    navigateToPharmacyDetails(event.pharmacy)
                }

                is PharmacyListSingleEvent.UpdateFabExpanded -> {
                    onExpandStateChanged(event.isExpanded)
                }

                is PharmacyListSingleEvent.ShowError -> {
                    alertMessage = event.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
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
                        viewModel.processIntent(PharmacyListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        PharmacyListContent(
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

        if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
    }
}

@Composable
private fun PharmacyListContent(
    modifier: Modifier = Modifier,
    state: PharmacyListState,
    onIntent: (PharmacyListIntent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val listState = rememberLazyListState()
        val expandedFabState =
            remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex == 0
                }
            }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(PharmacyListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (state.pharmacies.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(CoreR.string.no_pharmacies_data),
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    items = state.pharmacies,
                    key = { pharmacy ->
                        pharmacy.id
                    },
                ) { pharmacy ->
                    PharmacyView(pharmacy = pharmacy) {
                        onIntent(PharmacyListIntent.SelectPharmacy(pharmacy))
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
internal fun PharmacyContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        PharmacyListContent(
            state =
                PharmacyListState(
                    pharmacies =
                        listOf(
                            Pharmacy(
                                id = 1,
                                name = "صيدلية الحياة",
                                address = "38 ش فيصل - حسن  محمد",
                                phone = "123-456-7890",
                            ),
                            Pharmacy(
                                id = 2,
                                name = "صيدلية يحيى",
                                address = "45 ش أسامة أبو عميرة - متفرع من ش فيصل",
                                phone = "987-654-3210",
                            ),
                        ),
                ),
            onIntent = {},
        )
    }
}

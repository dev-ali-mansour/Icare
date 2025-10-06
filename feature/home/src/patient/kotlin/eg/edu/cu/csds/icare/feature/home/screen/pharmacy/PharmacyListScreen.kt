package eg.edu.cu.csds.icare.feature.home.screen.pharmacy

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacyView
import eg.edu.cu.csds.icare.core.ui.view.SearchTextField
import eg.edu.cu.csds.icare.feature.home.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyListScreen(
    viewModel: PharmacyListViewModel = koinViewModel(),
    onNavigationIconClicked: () -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(PharmacyListEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is PharmacyListEffect.OnBackClick -> onNavigationIconClicked()

                is PharmacyListEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_pharmacies),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = barBackgroundColor,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = { onNavigationIconClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.processEvent(PharmacyListEvent.Refresh)
                        },
                    ).padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxSize(),
            ) {
                val (line, content, refresh) = createRefs()

                Box(
                    modifier =
                        Modifier
                            .constrainAs(line) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }.background(Yellow500)
                            .fillMaxWidth()
                            .height(XS_PADDING),
                )

                PharmacyListContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
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
                        Modifier.constrainAs(refresh) {
                            top.linkTo(parent.top, margin = XL_PADDING)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    isRefreshing = uiState.isLoading,
                    state = refreshState,
                )

                if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
            }
        }
    }
}

@Composable
private fun PharmacyListContent(
    uiState: PharmacyListState,
    modifier: Modifier = Modifier,
    onEvent: (PharmacyListEvent) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            val (search, details) = createRefs()

            SearchTextField(
                modifier =
                    Modifier
                        .constrainAs(search) {
                            top.linkTo(parent.top, margin = M_PADDING)
                            start.linkTo(parent.start, M_PADDING)
                            end.linkTo(parent.end, M_PADDING)
                            width = Dimension.fillToConstraints
                        },
                placeholder =
                    stringResource(
                        eg.edu.cu.csds.icare.core.ui.R.string.core_ui_search_for_pharmacy,
                    ),
                value = uiState.searchQuery,
                focus = false,
                onValueChange = { onEvent(PharmacyListEvent.UpdateSearchQuery(it)) },
                onClear = { onEvent(PharmacyListEvent.UpdateSearchQuery("")) },
                onSearch = { },
            )

            if (uiState.pharmacies.isEmpty()) {
                EmptyContentView(
                    modifier =
                        Modifier
                            .constrainAs(details) {
                                top.linkTo(search.bottom, margin = M_PADDING)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                    text = stringResource(R.string.feature_home_no_data_matched),
                )
            } else {
                LazyColumn(
                    modifier =
                        modifier.constrainAs(details) {
                            top.linkTo(search.bottom, margin = M_PADDING)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    verticalArrangement = Arrangement.spacedBy(S_PADDING),
                ) {
                    items(uiState.pharmacies) { pharmacy ->
                        PharmacyView(
                            pharmacy = pharmacy,
                            modifier = modifier,
                            onClick = {},
                        )
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
private fun PharmacyListContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        PharmacyListContent(
            uiState =
                PharmacyListState(
                    pharmacies =
                        listOf(
                            Pharmacy(
                                name = "Pharmacy 1",
                                address = "Address 1",
                                phone = "123456789",
                                id = 1,
                            ),
                            Pharmacy(
                                name = "Pharmacy 2",
                                address = "Address 2",
                                phone = "987654321",
                                id = 2,
                            ),
                            Pharmacy(
                                name = "Pharmacy 3",
                                address = "Address 3",
                                phone = "555555555",
                                id = 3,
                            ),
                            Pharmacy(
                                name = "Pharmacy 4",
                                address = "Address 4",
                                phone = "111111111",
                            ),
                        ),
                ),
            onEvent = {},
        )
    }
}

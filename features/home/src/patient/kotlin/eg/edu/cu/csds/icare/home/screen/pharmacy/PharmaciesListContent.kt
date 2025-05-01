package eg.edu.cu.csds.icare.home.screen.pharmacy

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacyView
import eg.edu.cu.csds.icare.core.ui.view.SearchTextField
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun PharmaciesListContent(
    pharmacyRes: Resource<List<Pharmacy>>,
    searchQuery: String,
    showLoading: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
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
                placeholder = stringResource(CoreR.string.search_for_pharmacy),
                value = searchQuery,
                focus = false,
                onValueChange = { onSearchQueryChange(it) },
                onClear = { onClear() },
                onSearch = { onSearch() },
            )

            when (pharmacyRes) {
                is Resource.Unspecified -> LaunchedEffect(key1 = true) { showLoading(false) }
                is Resource.Loading -> LaunchedEffect(key1 = true) { showLoading(true) }

                is Resource.Success -> {
                    LaunchedEffect(key1 = true) { showLoading(false) }
                    pharmacyRes.data?.let { pharmacies ->

                        if (pharmacies.isEmpty()) {
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
                                text = stringResource(R.string.no_doctors_available),
                            )
                        } else {
                            PharmacyListContent(
                                pharmacies = pharmacies,
                                modifier =
                                    modifier.constrainAs(details) {
                                        top.linkTo(search.bottom, margin = M_PADDING)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                        width = Dimension.fillToConstraints
                                        height = Dimension.fillToConstraints
                                    },
                            )
                        }
                    }
                }

                is Resource.Error ->
                    LaunchedEffect(key1 = true) {
                        showLoading(false)
                        onError(pharmacyRes.error)
                    }
            }
        }
    }
}

@Composable
private fun PharmacyListContent(
    pharmacies: List<Pharmacy>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        items(pharmacies) { pharmacy ->
            PharmacyView(
                pharmacy = pharmacy,
                modifier = modifier,
                onClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
fun PharmacyListContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        PharmaciesListContent(
            pharmacyRes =
                Resource.Success(
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
            searchQuery = "",
            showLoading = {},
            onSearchQueryChange = {},
            onSearch = {},
            onClear = {},
            onError = {},
        )
    }
}

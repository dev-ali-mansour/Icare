package eg.edu.cu.csds.icare.home.screen.scan

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
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.CenterView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.SearchTextField
import eg.edu.cu.csds.icare.home.screen.lab.LabsListContent
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun ScanCentersListContent(
    centersRes: Resource<List<LabImagingCenter>>,
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
                placeholder = stringResource(CoreR.string.search_by_doctor_name_or_speciality),
                value = searchQuery,
                focus = false,
                onValueChange = { onSearchQueryChange(it) },
                onClear = { onClear() },
                onSearch = { onSearch() },
            )

            when (centersRes) {
                is Resource.Unspecified -> LaunchedEffect(key1 = true) { showLoading(false) }
                is Resource.Loading -> LaunchedEffect(key1 = true) { showLoading(true) }

                is Resource.Success -> {
                    LaunchedEffect(key1 = true) { showLoading(false) }
                    centersRes.data?.let { centers ->

                        if (centers.isEmpty()) {
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
                            CenterListContent(
                                centers = centers,
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
                        onError(centersRes.error)
                    }
            }
        }
    }
}

@Composable
private fun CenterListContent(
    centers: List<LabImagingCenter>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        items(centers) { center ->
            CenterView(
                center = center,
                modifier = modifier,
                showType = true,
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
fun ScanCentersListContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        LabsListContent(
            centersRes =
                Resource.Success(
                    listOf(
                        LabImagingCenter(
                            id = 1,
                            name = "Alfa",
                            type = 2,
                            phone = "123456789",
                            address = "Address 1",
                        ),
                        LabImagingCenter(
                            id = 1,
                            name = "Alfa",
                            type = 2,
                            phone = "123498789",
                            address = "Address 1",
                        ),
                        LabImagingCenter(
                            id = 2,
                            name = "El-Borg",
                            type = 2,
                            phone = "123456789",
                            address = "Address 1",
                        ),
                        LabImagingCenter(
                            id = 3,
                            name = "El-Shams",
                            type = 2,
                            phone = "123456789",
                            address = "Address 1",
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

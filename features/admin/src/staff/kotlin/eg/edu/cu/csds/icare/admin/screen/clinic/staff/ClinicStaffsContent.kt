package eg.edu.cu.csds.icare.admin.screen.clinic.staff

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.ClinicStaffView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun ClinicStaffsContent(
    modifier: Modifier = Modifier,
    staffsResource: Resource<List<ClinicStaff>>,
    actionResource: Resource<Nothing?>,
    showLoading: (Boolean) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onItemClicked: (ClinicStaff) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (emptyContent, list) = createRefs()

        when (staffsResource) {
            is Resource.Unspecified -> LaunchedEffect(key1 = staffsResource) { showLoading(false) }
            is Resource.Loading -> LaunchedEffect(key1 = staffsResource) { showLoading(true) }

            is Resource.Success -> {
                LaunchedEffect(key1 = staffsResource) { showLoading(false) }
                staffsResource.data?.let { staff ->
                    val listState = rememberLazyListState()
                    val expandedFabState =
                        remember {
                            derivedStateOf {
                                listState.firstVisibleItemIndex == 0
                            }
                        }
                    LaunchedEffect(key1 = expandedFabState.value) {
                        onExpandStateChanged(expandedFabState.value)
                    }

                    if (staff.isEmpty()) {
                        EmptyContentView(
                            modifier =
                                Modifier.constrainAs(emptyContent) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                            text = stringResource(CoreR.string.no_clinic_staff_data),
                        )
                    } else {
                        LazyColumn(
                            modifier =
                                Modifier.constrainAs(list) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(S_PADDING),
                        ) {
                            items(
                                items = staff,
                                key = { staff ->
                                    staff.id
                                },
                            ) { staff ->
                                ClinicStaffView(clinicStaff = staff) {
                                    onItemClicked(staff)
                                }
                            }
                        }
                    }
                }
            }

            is Resource.Error ->
                LaunchedEffect(key1 = staffsResource) {
                    onError(staffsResource.error)
                }
        }

        when (actionResource) {
            is Resource.Unspecified -> LaunchedEffect(key1 = actionResource) { showLoading(false) }
            is Resource.Loading -> LaunchedEffect(key1 = actionResource) { showLoading(true) }

            is Resource.Success ->
                LaunchedEffect(key1 = Unit) {
                    showLoading(false)
                }

            is Resource.Error ->
                LaunchedEffect(key1 = actionResource) {
                    showLoading(false)
                    onError(actionResource.error)
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
        ClinicStaffsContent(
            modifier = Modifier,
            staffsResource = Resource.Success(data = listOf()),
            actionResource = Resource.Success(null),
            showLoading = {},
            onExpandStateChanged = {},
            onItemClicked = {},
        ) {}
    }
}

package eg.edu.cu.csds.icare.admin.screen.center.staff

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.CenterStaffView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun CenterStaffsContent(
    modifier: Modifier = Modifier,
    staffsResource: Resource<List<CenterStaff>>,
    actionResource: Resource<Nothing?>,
    onExpandStateChanged: (Boolean) -> Unit,
    onItemClicked: (CenterStaff) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (progress, emptyContent, list) = createRefs()

        when (staffsResource) {
            is Resource.Unspecified -> {}
            is Resource.Loading ->
                CircularProgressIndicator(
                    modifier =
                        Modifier.constrainAs(progress) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                )

            is Resource.Success -> {
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
                            text = stringResource(CoreR.string.no_center_staff_data),
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
                                CenterStaffView(
                                    modifier =
                                        Modifier.clickable {
                                            onItemClicked(staff)
                                        },
                                    staff = staff,
                                )
                            }
                        }
                    }
                }
            }

            is Resource.Error ->
                LaunchedEffect(key1 = true) {
                    onError(staffsResource.error)
                }
        }

        when (actionResource) {
            is Resource.Unspecified -> {}
            is Resource.Loading ->
                CircularProgressIndicator(
                    modifier =
                        Modifier.constrainAs(
                            progress,
                        ) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                )

            is Resource.Success -> {}

            is Resource.Error ->
                LaunchedEffect(key1 = true) {
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
        CenterStaffsContent(
            modifier = Modifier,
            staffsResource = Resource.Success(data = listOf()),
            actionResource = Resource.Success(null),
            onExpandStateChanged = {},
            onItemClicked = {},
        ) {}
    }
}

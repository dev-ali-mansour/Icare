package eg.edu.cu.csds.icare.admin.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.admin.screen.center.CentersContent
import eg.edu.cu.csds.icare.admin.screen.center.staff.CenterStaffsContent
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicsContent
import eg.edu.cu.csds.icare.admin.screen.clinic.doctor.DoctorsContent
import eg.edu.cu.csds.icare.admin.screen.clinic.staff.ClinicStaffsContent
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacistContent
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyContent
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.common.SectionCategory
import eg.edu.cu.csds.icare.core.ui.theme.MEDIUM_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
internal fun AdminContent(
    modifier: Modifier = Modifier,
    categories: List<SectionCategory>,
    selectedCategoryTabIndex: Int,
    selectedSectionTabIndex: Int,
    clinicsResource: Resource<List<Clinic>>,
    doctorsResource: Resource<List<Doctor>>,
    clinicStaffsResource: Resource<List<ClinicStaff>>,
    pharmaciesResource: Resource<List<Pharmacy>>,
    pharmacistsResource: Resource<List<Pharmacist>>,
    centersResource: Resource<List<LabImagingCenter>>,
    centerStaffsResource: Resource<List<CenterStaff>>,
    actionResource: Resource<Nothing?>,
    onCategoryTabClicked: (Int) -> Unit,
    onSectionTabClicked: (Int) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onClinicClicked: (Clinic) -> Unit,
    onDoctorClicked: (Doctor) -> Unit,
    onClinicStaffClicked: (ClinicStaff) -> Unit,
    onPharmacyClicked: (Pharmacy) -> Unit,
    onPharmacistClicked: (Pharmacist) -> Unit,
    onCenterClicked: (LabImagingCenter) -> Unit,
    onCenterStaffClicked: (CenterStaff) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .padding(start = S_PADDING, end = S_PADDING, bottom = S_PADDING)
                .verticalScroll(rememberScrollState()),
    ) {
        val (tabRow, subTabRow, content) = createRefs()

        ScrollableTabRow(
            selectedTabIndex = selectedCategoryTabIndex,
            modifier =
                Modifier.constrainAs(tabRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            containerColor = contentBackgroundColor,
            contentColor = Yellow700,
            indicator = { tabPositions ->
                Box(
                    modifier =
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedCategoryTabIndex])
                            .height(TAB_INDICATOR_HEIGHT)
                            .clip(RoundedCornerShape(TAB_INDICATOR_ROUND_CORNER_SIZE))
                            .background(
                                color = barBackgroundColor,
                                shape = RoundedCornerShape(TAB_INDICATOR_ROUND_CORNER_SIZE),
                            ),
                )
            },
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = selectedCategoryTabIndex == index,
                    onClick = {
                        onCategoryTabClicked(index)
                    },
                    text = {
                        Text(
                            text = stringResource(category.titleResId),
                            color = barBackgroundColor,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = helveticaFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    },
                    selectedContentColor = barBackgroundColor,
                    unselectedContentColor = barBackgroundColor,
                )
            }
        }

        TabRow(
            selectedTabIndex = selectedSectionTabIndex,
            modifier =
                Modifier.constrainAs(subTabRow) {
                    top.linkTo(tabRow.bottom, XS_PADDING)
                    start.linkTo(tabRow.start)
                    end.linkTo(tabRow.end)
                    width = Dimension.fillToConstraints
                },
            containerColor = contentBackgroundColor,
            contentColor = Yellow700,
            indicator = { tabPositions ->
                Box(
                    modifier =
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedSectionTabIndex])
                            .height(TAB_INDICATOR_HEIGHT)
                            .clip(RoundedCornerShape(TAB_INDICATOR_ROUND_CORNER_SIZE))
                            .background(
                                color = barBackgroundColor,
                                shape = RoundedCornerShape(TAB_INDICATOR_ROUND_CORNER_SIZE),
                            ),
                )
            },
        ) {
            categories[selectedCategoryTabIndex].sections.forEachIndexed { index, section ->
                Tab(
                    selected = selectedSectionTabIndex == index,
                    onClick = { onSectionTabClicked(index) },
                    text = {
                        Text(
                            text = stringResource(section.titleResId),
                            color = barBackgroundColor,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = helveticaFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    },
                    icon = {
                        Icon(
                            painterResource(id = section.iconResId),
                            modifier = Modifier.size(MEDIUM_ICON_SIZE),
                            contentDescription = null,
                        )
                    },
                    selectedContentColor = barBackgroundColor,
                    unselectedContentColor = barBackgroundColor,
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .constrainAs(content) {
                        top.linkTo(subTabRow.bottom, S_PADDING)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
        ) {
            when (selectedCategoryTabIndex) {
                0 ->
                    when (selectedSectionTabIndex) {
                        0 ->
                            ClinicsContent(
                                modifier = Modifier,
                                clinicsResource = clinicsResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onClinicClicked(it) },
                                onError = onError,
                            )

                        1 ->
                            DoctorsContent(
                                modifier = Modifier,
                                doctorsResource = doctorsResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onDoctorClicked(it) },
                                onError = onError,
                            )

                        2 ->
                            ClinicStaffsContent(
                                modifier = Modifier,
                                staffsResource = clinicStaffsResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onClinicStaffClicked(it) },
                                onError = onError,
                            )
                    }

                1 ->
                    when (selectedSectionTabIndex) {
                        0 ->
                            PharmacyContent(
                                modifier = Modifier,
                                pharmaciesResource = pharmaciesResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onPharmacyClicked(it) },
                                onError = onError,
                            )

                        1 ->
                            PharmacistContent(
                                modifier = Modifier,
                                pharmacistsResource = pharmacistsResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onPharmacistClicked(it) },
                                onError = onError,
                            )
                    }

                2 ->
                    when (selectedSectionTabIndex) {
                        0 ->
                            CentersContent(
                                modifier = Modifier,
                                centersResource = centersResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onCenterClicked(it) },
                                onError = onError,
                            )

                        1 ->
                            CenterStaffsContent(
                                modifier = Modifier,
                                staffsResource = centerStaffsResource,
                                actionResource = actionResource,
                                onExpandStateChanged = { onExpandStateChanged(it) },
                                onItemClicked = { onCenterStaffClicked(it) },
                                onError = onError,
                            )
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
internal fun AdminContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        AdminContent(
            categories =
                listOf(),
            selectedCategoryTabIndex = 0,
            selectedSectionTabIndex = 0,
            clinicsResource = Resource.Success(listOf()),
            doctorsResource = Resource.Success(listOf()),
            clinicStaffsResource = Resource.Success(listOf()),
            pharmaciesResource = Resource.Success(listOf()),
            pharmacistsResource = Resource.Success(listOf()),
            centersResource = Resource.Success(listOf()),
            centerStaffsResource = Resource.Success(listOf()),
            actionResource = Resource.Success(null),
            onCategoryTabClicked = {},
            onSectionTabClicked = {},
            onExpandStateChanged = {},
            onClinicClicked = {},
            onDoctorClicked = {},
            onClinicStaffClicked = {},
            onPharmacyClicked = {},
            onPharmacistClicked = {},
            onCenterClicked = {},
            onCenterStaffClicked = {},
            onError = {},
        )
    }
}

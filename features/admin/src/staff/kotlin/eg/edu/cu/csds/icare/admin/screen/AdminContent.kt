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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.admin.screen.center.list.CenterListSection
import eg.edu.cu.csds.icare.admin.screen.clinic.list.ClinicListSection
import eg.edu.cu.csds.icare.admin.screen.clinician.list.ClinicianListSection
import eg.edu.cu.csds.icare.admin.screen.doctor.list.DoctorListSection
import eg.edu.cu.csds.icare.admin.screen.pharmacist.list.PharmacistListSection
import eg.edu.cu.csds.icare.admin.screen.pharmacy.list.PharmacyListSection
import eg.edu.cu.csds.icare.admin.screen.staff.list.StaffListSection
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.common.SectionCategory
import eg.edu.cu.csds.icare.core.ui.common.SectionItem
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
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon

@Composable
internal fun AdminContent(
    modifier: Modifier = Modifier,
    selectedCategoryTabIndex: Int,
    selectedSectionTabIndex: Int,
    onCategoryTabClicked: (Int) -> Unit,
    onSectionTabClicked: (Int) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    navigateToClinicDetails: (Clinic) -> Unit,
    navigateToDoctorDetails: (Doctor) -> Unit,
    navigateToClinicianDetails: (Clinician) -> Unit,
    navigateToPharmacyDetails: (Pharmacy) -> Unit,
    navigateToPharmacistDetails: (Pharmacist) -> Unit,
    navigateToCenterDetails: (LabImagingCenter) -> Unit,
    navigateToStaffDetails: (Staff) -> Unit,
) {
    rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

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
            adminCategories.forEachIndexed { index, category ->
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
            adminCategories[selectedCategoryTabIndex].sections.forEachIndexed { index, section ->
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
                            ClinicListSection(
                                navigateToClinicDetails = { navigateToClinicDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )

                        1 ->
                            DoctorListSection(
                                navigateToDoctorDetails = { navigateToDoctorDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )

                        2 ->
                            ClinicianListSection(
                                navigateToClinicianDetails = { navigateToClinicianDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )
                    }

                1 ->
                    when (selectedSectionTabIndex) {
                        0 ->
                            PharmacyListSection(
                                navigateToPharmacyDetails = { navigateToPharmacyDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )

                        1 ->
                            PharmacistListSection(
                                navigateToPharmacistDetails = { navigateToPharmacistDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )
                    }

                2 ->
                    when (selectedSectionTabIndex) {
                        0 ->
                            CenterListSection(
                                navigateToCenterDetails = { navigateToCenterDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )

                        1 ->
                            StaffListSection(
                                navigateToStaffDetails = { navigateToStaffDetails(it) },
                                onExpandStateChanged = { onExpandStateChanged(it) },
                            )
                    }
            }
        }
    }

    if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun AdminContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        AdminContent(
            selectedCategoryTabIndex = 0,
            selectedSectionTabIndex = 0,
            onCategoryTabClicked = {},
            onSectionTabClicked = {},
            onExpandStateChanged = {},
            navigateToClinicDetails = {},
            navigateToDoctorDetails = {},
            navigateToClinicianDetails = {},
            navigateToPharmacyDetails = {},
            navigateToPharmacistDetails = {},
            navigateToCenterDetails = {},
            navigateToStaffDetails = {},
        )
    }
}

val adminCategories =
    listOf(
        SectionCategory(
            titleResId = R.string.clinics,
            sections =
                listOf(
                    SectionItem(
                        iconResId = R.drawable.ic_clinic,
                        titleResId = R.string.clinics,
                    ),
                    SectionItem(
                        iconResId = R.drawable.ic_doctor,
                        titleResId = R.string.doctors,
                    ),
                    SectionItem(
                        iconResId = R.drawable.ic_staff,
                        titleResId = R.string.clinic_staffs,
                    ),
                ),
        ),
        SectionCategory(
            titleResId = R.string.pharmacies,
            sections =
                listOf(
                    SectionItem(
                        iconResId = R.drawable.ic_pharmacy,
                        titleResId = R.string.pharmacies,
                    ),
                    SectionItem(
                        iconResId = R.drawable.ic_pharmacist,
                        titleResId = R.string.pharmacists,
                    ),
                ),
        ),
        SectionCategory(
            titleResId = R.string.centers,
            sections =
                listOf(
                    SectionItem(
                        iconResId = R.drawable.ic_lab,
                        titleResId = R.string.centers,
                    ),
                    SectionItem(
                        iconResId = R.drawable.ic_staff2,
                        titleResId = R.string.center_staffs,
                    ),
                ),
        ),
    )

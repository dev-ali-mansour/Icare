package eg.edu.cu.csds.icare.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AdminScreen(
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
    navigateToClinicDetails: (Clinic) -> Unit,
    navigateToDoctorDetails: (Doctor) -> Unit,
    navigateToClinicianDetails: (Clinician) -> Unit,
    navigateToPharmacyDetails: (Pharmacy) -> Unit,
    navigateToPharmacistDetails: (Pharmacist) -> Unit,
    navigateToCenterDetails: (LabImagingCenter) -> Unit,
    navigateToStaffDetails: (Staff) -> Unit,
) {
    var selectedCategoryTabIndex by remember { mutableIntStateOf(0) }
    var selectedSectionTabIndex by remember { mutableIntStateOf(0) }
    var expandedFab by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = CoreR.string.sections_admin)) },
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(CoreR.string.add),
                        color = Color.White,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White,
                    )
                },
                onClick = {
                    when (selectedCategoryTabIndex) {
                        0 ->
                            when (selectedSectionTabIndex) {
                                0 -> navigateToRoute(Route.NewClinic)
                                1 -> navigateToRoute(Route.NewDoctor)
                                2 -> navigateToRoute(Route.NewClinician)
                            }

                        1 ->
                            when (selectedSectionTabIndex) {
                                0 -> navigateToRoute(Route.NewPharmacy)
                                1 -> navigateToRoute(Route.NewPharmacist)
                            }

                        2 ->
                            when (selectedSectionTabIndex) {
                                0 -> navigateToRoute(Route.NewCenter)
                                1 -> navigateToRoute(Route.NewStaff)
                            }
                    }
                },
                containerColor = barBackgroundColor,
                expanded = expandedFab,
            )
        },
    ) { paddingValues ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxWidth(),
            ) {
                val (_, line, content) = createRefs()
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

                AdminContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    selectedCategoryTabIndex = selectedCategoryTabIndex,
                    selectedSectionTabIndex = selectedSectionTabIndex,
                    onCategoryTabClicked = {
                        selectedCategoryTabIndex = it
                    },
                    onSectionTabClicked = {
                        selectedSectionTabIndex = it
                    },
                    onExpandStateChanged = { expandedFab = it },
                    navigateToClinicDetails = { navigateToClinicDetails(it) },
                    navigateToDoctorDetails = { navigateToDoctorDetails(it) },
                    navigateToClinicianDetails = { navigateToClinicianDetails(it) },
                    navigateToPharmacyDetails = { navigateToPharmacyDetails(it) },
                    navigateToPharmacistDetails = { navigateToPharmacistDetails(it) },
                    navigateToCenterDetails = { navigateToCenterDetails(it) },
                    navigateToStaffDetails = { navigateToStaffDetails(it) },
                )
            }
        }
    }
}

package eg.edu.cu.csds.icare.feature.admin.screen

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.CommonTopAppBar
import eg.edu.cu.csds.icare.core.ui.navigation.Route

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
    val context: Context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedCategoryTabIndex by remember { mutableIntStateOf(0) }
    var selectedSectionTabIndex by remember { mutableIntStateOf(0) }
    var expandedFab by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CommonTopAppBar(title = stringResource(id = string.core_ui_sections_admin)) {
                onNavigationIconClicked()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(string.core_ui_add),
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
                        0 -> {
                            when (selectedSectionTabIndex) {
                                0 -> navigateToRoute(Route.NewClinic)
                                1 -> navigateToRoute(Route.NewDoctor)
                                2 -> navigateToRoute(Route.NewClinician)
                            }
                        }

                        1 -> {
                            when (selectedSectionTabIndex) {
                                0 -> navigateToRoute(Route.NewPharmacy)
                                1 -> navigateToRoute(Route.NewPharmacist)
                            }
                        }

                        2 -> {
                            when (selectedSectionTabIndex) {
                                0 -> navigateToRoute(Route.NewCenter)
                                1 -> navigateToRoute(Route.NewStaff)
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                expanded = expandedFab,
            )
        },
    ) { innerPadding ->
        AdminContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            selectedCategoryTabIndex = selectedCategoryTabIndex,
            selectedSectionTabIndex = selectedSectionTabIndex,
            onCategoryTabClicked = {
                selectedSectionTabIndex = 0
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
            onError = { message ->
                snackbarHostState.showSnackbar(
                    message = message.asString(context),
                    duration = SnackbarDuration.Short,
                )
            },
        )
    }
}

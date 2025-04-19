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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AdminScreen(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    onNavigationIconClicked: () -> Unit,
    onFabClicked: () -> Unit,
    onCategoryTabClicked: () -> Unit,
    onSectionTabClicked: () -> Unit,
    onClinicClicked: (Clinic) -> Unit,
    onDoctorClicked: (Doctor) -> Unit,
    onClinicStaffClicked: (ClinicStaff) -> Unit,
    onPharmacyClicked: (Pharmacy) -> Unit,
    onPharmacistClicked: (Pharmacist) -> Unit,
    onCenterClicked: (LabImagingCenter) -> Unit,
    onCenterStaffClicked: (CenterStaff) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    val categories = mainViewModel.adminCategories
    val clinicsResource by clinicViewModel.clinicsResFlow.collectAsStateWithLifecycle()
    val doctorsResource by clinicViewModel.doctorsResFlow.collectAsStateWithLifecycle()
    val clinicStaffsResource by clinicViewModel.clinicStaffsResFlow.collectAsStateWithLifecycle()
    val pharmaciesResource by pharmacyViewModel.pharmaciesResFlow.collectAsStateWithLifecycle()
    val pharmacistsResource by pharmacyViewModel.pharmacistsResFlow.collectAsStateWithLifecycle()
    val centersResource by centerViewModel.centersResFlow.collectAsStateWithLifecycle()
    val centerStaffsResource by centerViewModel.centerStaffsResFlow.collectAsStateWithLifecycle()
    val actionResource by clinicViewModel.actionResFlow.collectAsStateWithLifecycle(initialValue = Resource.Unspecified())
    var selectedCategoryTabIndex by mainViewModel.selectedCategoryTabIndex
    var selectedSectionTabIndex by mainViewModel.selectedSectionTabIndex
    var expandedFab by clinicViewModel.expandedFab

    LaunchedEffect(key1 = Unit) {
        clinicViewModel.listClinics()
    }

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
                onClick = { onFabClicked() },
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
                val (line, content) = createRefs()
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
                    categories = categories,
                    selectedCategoryTabIndex = selectedCategoryTabIndex,
                    selectedSectionTabIndex = selectedSectionTabIndex,
                    clinicsResource = clinicsResource,
                    doctorsResource = doctorsResource,
                    clinicStaffsResource = clinicStaffsResource,
                    pharmaciesResource = pharmaciesResource,
                    pharmacistsResource = pharmacistsResource,
                    centersResource = centersResource,
                    centerStaffsResource = centerStaffsResource,
                    actionResource = actionResource,
                    onCategoryTabClicked = {
                        selectedCategoryTabIndex = it
                        onCategoryTabClicked()
                    },
                    onSectionTabClicked = {
                        selectedSectionTabIndex = it
                        onSectionTabClicked()
                    },
                    onExpandStateChanged = { expandedFab = it },
                    onClinicClicked = { onClinicClicked(it) },
                    onDoctorClicked = { onDoctorClicked(it) },
                    onClinicStaffClicked = { onClinicStaffClicked(it) },
                    onPharmacyClicked = { onPharmacyClicked(it) },
                    onPharmacistClicked = { onPharmacistClicked(it) },
                    onCenterClicked = { onCenterClicked(it) },
                    onCenterStaffClicked = { onCenterStaffClicked(it) },
                    onError = onError,
                )
            }
        }
    }
}

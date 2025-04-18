package eg.edu.cu.csds.icare.admin.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.AdminScreen
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.EditClinicScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.NewClinicScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.adminRoute(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    centerViewModel: CenterViewModel,
    pharmacyViewModel: PharmacyViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.Admin> {
        AdminScreen(
            mainViewModel = mainViewModel,
            clinicViewModel = clinicViewModel,
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onFabClicked = {
                when (mainViewModel.selectedCategoryTabIndex.intValue) {
                    0 ->
                        when (mainViewModel.selectedSectionTabIndex.intValue) {
                            0 -> navigateToScreen(Screen.NewClinic)
                            1 -> navigateToScreen(Screen.NewDoctor)
                            2 -> navigateToScreen(Screen.NewClinicStaff)
                        }

                    1 ->
                        when (mainViewModel.selectedSectionTabIndex.intValue) {
                            0 -> navigateToScreen(Screen.NewPharmacy)
                            1 -> navigateToScreen(Screen.NewPharmacist)
                        }

                    2 ->
                        when (mainViewModel.selectedSectionTabIndex.intValue) {
                            0 -> navigateToScreen(Screen.NewCenter)
                            1 -> navigateToScreen(Screen.NewCenterStaff)
                        }
                }
            },
            onCategoryTabClicked = {
                mainViewModel.selectedSectionTabIndex.intValue = 0
                when (mainViewModel.selectedCategoryTabIndex.intValue) {
                    0 -> clinicViewModel.listClinics()
                    1 -> pharmacyViewModel.listPharmacies()
                    2 -> centerViewModel.listCenters()
                }
            },
            onSectionTabClicked = {
                when (mainViewModel.selectedCategoryTabIndex.intValue) {
                    0 ->
                        when (mainViewModel.selectedSectionTabIndex.intValue) {
                            0 -> clinicViewModel.listClinics()
                            1 -> clinicViewModel.listDoctors()
                            2 -> clinicViewModel.listStaffs()
                        }

                    1 ->
                        when (mainViewModel.selectedSectionTabIndex.intValue) {
                            0 -> pharmacyViewModel.listPharmacies()
                            1 -> pharmacyViewModel.listPharmacists()
                        }

                    2 ->
                        when (mainViewModel.selectedSectionTabIndex.intValue) {
                            0 -> centerViewModel.listCenters()
                            1 -> centerViewModel.listStaff()
                        }
                }
            },
            onClinicClicked = { clinic ->
                clinicViewModel.selectedClinicState.value = clinic
                navigateToScreen(Screen.EditClinic)
            },
            onDoctorClicked = { doctor ->
                clinicViewModel.selectedDoctorState.value = doctor
                navigateToScreen(Screen.EditDoctor)
            },
            onClinicStaffClicked = { staff ->
                clinicViewModel.selectedClinicStaffState.value = staff
                navigateToScreen(Screen.EditClinicStaff)
            },
            onPharmacyClicked = { pharmacy ->
                pharmacyViewModel.selectedPharmacyState.value = pharmacy
                navigateToScreen(Screen.EditPharmacy)
            },
            onPharmacistClicked = { pharmacist ->
                pharmacyViewModel.selectedPharmacistState.value = pharmacist
                navigateToScreen(Screen.EditPharmacist)
            },
            onCenterClicked = { center ->
                centerViewModel.selectedCenterState.value = center
                navigateToScreen(Screen.EditCenter)
            },
            onCenterStaffClicked = { staff ->
                centerViewModel.selectedCenterStaffState.value = staff
                navigateToScreen(Screen.EditCenterStaff)
            },
            onError = { onError(it) },
        )
    }

    composable<Screen.NewClinic> {
        NewClinicScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { clinicViewModel.addNewClinic() },
            onSuccess = {
//                clinicViewModel.listClinics()
                onNavigationIconClicked
            },
            onError = { onError(it) },
        )
    }

    composable<Screen.EditClinic> {
        EditClinicScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { clinicViewModel.updateClinic() },
            onSuccess = {
                clinicViewModel.listClinics()
                onNavigationIconClicked
            },
            onError = { onError(it) },
        )
    }

    composable<Screen.NewCenter> {
    }

    composable<Screen.EditCenter> {
    }

    composable<Screen.NewPharmacy> {
    }

    composable<Screen.EditPharmacy> {
    }
    composable<Screen.NewDoctor> {
    }

    composable<Screen.EditDoctor> {
    }

    composable<Screen.NewClinicStaff> {
    }

    composable<Screen.EditClinicStaff> {
    }

    composable<Screen.NewPharmacist> {
    }

    composable<Screen.EditPharmacist> {
    }

    composable<Screen.NewCenterStaff> {
    }

    composable<Screen.EditCenterStaff> {
    }
}

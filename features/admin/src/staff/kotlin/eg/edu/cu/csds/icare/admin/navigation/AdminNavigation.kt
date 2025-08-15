package eg.edu.cu.csds.icare.admin.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.AdminScreen
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.center.EditCenterScreen
import eg.edu.cu.csds.icare.admin.screen.center.NewCenterScreen
import eg.edu.cu.csds.icare.admin.screen.center.staff.EditCenterStaffScreen
import eg.edu.cu.csds.icare.admin.screen.center.staff.NewCenterStaffScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.EditClinicScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.NewClinicScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.doctor.add.NewDoctorScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.doctor.update.EditDoctorScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.staff.EditClinicStaffScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.staff.NewClinicStaffScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.EditPharmacyScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.NewPharmacyScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.pharmacist.EditPharmacistScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.pharmacist.NewPharmacistScreen
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route

fun NavGraphBuilder.adminRoute(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    centerViewModel: CenterViewModel,
    pharmacyViewModel: PharmacyViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Route) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Route.Admin> {
        AdminScreen(
            mainViewModel = mainViewModel,
            clinicViewModel = clinicViewModel,
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onRefresh = {
                loadData(
                    mainViewModel = mainViewModel,
                    clinicViewModel = clinicViewModel,
                    pharmacyViewModel = pharmacyViewModel,
                    centerViewModel = centerViewModel,
                    forceRefresh = true,
                )
            },
            onFabClicked = {
                onFabClicked(mainViewModel = mainViewModel) {
                    navigateToScreen(it)
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
                loadData(
                    mainViewModel = mainViewModel,
                    clinicViewModel = clinicViewModel,
                    pharmacyViewModel = pharmacyViewModel,
                    centerViewModel = centerViewModel,
                )
            },
            onClinicClicked = { clinic ->
                clinicViewModel.selectedClinicState.value = clinic
                navigateToScreen(Route.EditClinic)
            },
            onDoctorClicked = { doctor ->
                clinicViewModel.selectedDoctorState.value = doctor
                navigateToScreen(Route.EditDoctor)
            },
            onClinicStaffClicked = { staff ->
                clinicViewModel.selectedClinicStaffState.value = staff
                navigateToScreen(Route.EditClinicStaff)
            },
            onPharmacyClicked = { pharmacy ->
                pharmacyViewModel.selectedPharmacyState.value = pharmacy
                navigateToScreen(Route.EditPharmacy)
            },
            onPharmacistClicked = { pharmacist ->
                pharmacyViewModel.selectedPharmacistState.value = pharmacist
                navigateToScreen(Route.EditPharmacist)
            },
            onCenterClicked = { center ->
                centerViewModel.selectedCenterState.value = center
                navigateToScreen(Route.EditCenter)
            },
            onCenterStaffClicked = { staff ->
                centerViewModel.selectedCenterStaffState.value = staff
                navigateToScreen(Route.EditCenterStaff)
            },
            onError = { onError(it) },
        )
    }

    composable<Route.NewClinic> {
        NewClinicScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { clinicViewModel.addNewClinic() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.EditClinic> {
        EditClinicScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { clinicViewModel.updateClinic() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.NewCenter> {
        NewCenterScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { centerViewModel.addNewCenter() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.EditCenter> {
        EditCenterScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { centerViewModel.updateCenter() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.NewPharmacy> {
        NewPharmacyScreen(
            pharmacyViewModel = pharmacyViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { pharmacyViewModel.addNewPharmacy() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.EditPharmacy> {
        EditPharmacyScreen(
            pharmacyViewModel = pharmacyViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { pharmacyViewModel.updatePharmacy() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }
    composable<Route.NewDoctor> {
        NewDoctorScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }

    composable<Route.EditDoctor> {
        EditDoctorScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }

    composable<Route.NewClinicStaff> {
        NewClinicStaffScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { clinicViewModel.addNewStaff() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.EditClinicStaff> {
        EditClinicStaffScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { clinicViewModel.updateStaff() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.NewPharmacist> {
        NewPharmacistScreen(
            pharmacyViewModel = pharmacyViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { pharmacyViewModel.addNewPharmacist() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.EditPharmacist> {
        EditPharmacistScreen(
            pharmacyViewModel = pharmacyViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { pharmacyViewModel.updatePharmacist() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.NewCenterStaff> {
        NewCenterStaffScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { centerViewModel.addNewStaff() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Route.EditCenterStaff> {
        EditCenterStaffScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { centerViewModel.updateStaff() },
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }
}

private fun NavGraphBuilder.onFabClicked(
    mainViewModel: MainViewModel,
    navigateToScreen: (Route) -> Unit,
) {
    when (mainViewModel.selectedCategoryTabIndex.intValue) {
        0 ->
            when (mainViewModel.selectedSectionTabIndex.intValue) {
                0 -> navigateToScreen(Route.NewClinic)
                1 -> navigateToScreen(Route.NewDoctor)
                2 -> navigateToScreen(Route.NewClinicStaff)
            }

        1 ->
            when (mainViewModel.selectedSectionTabIndex.intValue) {
                0 -> navigateToScreen(Route.NewPharmacy)
                1 -> navigateToScreen(Route.NewPharmacist)
            }

        2 ->
            when (mainViewModel.selectedSectionTabIndex.intValue) {
                0 -> navigateToScreen(Route.NewCenter)
                1 -> navigateToScreen(Route.NewCenterStaff)
            }
    }
}

private fun NavGraphBuilder.loadData(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    forceRefresh: Boolean = false,
) {
    when (mainViewModel.selectedCategoryTabIndex.intValue) {
        0 ->
            when (mainViewModel.selectedSectionTabIndex.intValue) {
                0 -> clinicViewModel.listClinics(forceRefresh = forceRefresh)
                1 -> clinicViewModel.listDoctors(forceRefresh = forceRefresh)
                2 -> clinicViewModel.listStaffs()
            }

        1 ->
            when (mainViewModel.selectedSectionTabIndex.intValue) {
                0 -> pharmacyViewModel.listPharmacies(forceRefresh = forceRefresh)
                1 -> pharmacyViewModel.listPharmacists()
            }

        2 ->
            when (mainViewModel.selectedSectionTabIndex.intValue) {
                0 -> centerViewModel.listCenters(forceRefresh = forceRefresh)
                1 -> centerViewModel.listStaff()
            }
    }
}

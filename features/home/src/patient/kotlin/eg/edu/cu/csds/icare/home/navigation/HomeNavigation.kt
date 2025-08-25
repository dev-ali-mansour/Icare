package eg.edu.cu.csds.icare.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.screen.HomeScreen
import eg.edu.cu.csds.icare.home.screen.lab.LabsListScreen
import eg.edu.cu.csds.icare.home.screen.pharmacy.PharmaciesListScreen
import eg.edu.cu.csds.icare.home.screen.scan.ScanCentersListScreen

fun NavGraphBuilder.homeRoute(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    clinicViewModel: ClinicViewModel,
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    appointmentViewModel: AppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.Home> {
        HomeScreen(
            mainViewModel = mainViewModel,
            homeViewModel = homeViewModel,
            appointmentViewModel = appointmentViewModel,
            clinicViewModel = clinicViewModel,
            navigateToScreen = {
                when (it) {
                    Screen.ScanCenters -> centerViewModel.listImagingCenters()
                    Screen.DoctorList -> clinicViewModel.listDoctors()
                    Screen.LabCenters -> centerViewModel.listLabCenters()
                    Screen.Pharmacies -> pharmacyViewModel.listPharmacies()
                    else -> {}
                }
                navigateToScreen(it)
            },
            onDoctorClicked = {
                clinicViewModel.selectedDoctorState.value = it
                clinicViewModel.getDoctorSchedule(it.id)
                navigateToScreen(Screen.DoctorProfile)
            },
            onError = { onError(it) },
        )
    }

    composable<Screen.LabCenters> {
        LabsListScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = { centerViewModel.searchLabCenters() },
            onClear = { centerViewModel.listLabCenters() },
            onError = { onError(it) },
        )
    }

    composable<Screen.ScanCenters> {
        ScanCentersListScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = { centerViewModel.searchImagingCenters() },
            onClear = { centerViewModel.listImagingCenters() },
            onError = { onError(it) },
        )
    }

    composable<Screen.Pharmacies> {
        PharmaciesListScreen(
            pharmacyViewModel = pharmacyViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = { pharmacyViewModel.searchPharmacies() },
            onClear = { pharmacyViewModel.listPharmacies() },
            onError = { onError(it) },
        )
    }
}

package eg.edu.cu.csds.icare.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.home.screen.home.HomeScreen
import eg.edu.cu.csds.icare.home.screen.lab.LabsListScreen
import eg.edu.cu.csds.icare.home.screen.pharmacy.PharmaciesListScreen
import eg.edu.cu.csds.icare.home.screen.scan.ScanCentersListScreen

fun NavGraphBuilder.homeRoute(
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Route) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Route.Home> {
        HomeScreen(
            navigateToScreen = { navigateToScreen(it) },
            navigateToDoctorDetails = {
                // Todo: Use SelectedDoctorViewModel instead
//                clinicViewModel.selectedDoctorState.value = it
//                clinicViewModel.getDoctorSchedule(it.id)
                navigateToScreen(Route.DoctorProfile)
            },
        )
    }

    composable<Route.LabCenters> {
        LabsListScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = { centerViewModel.searchLabCenters() },
            onClear = { centerViewModel.listLabCenters() },
            onError = { onError(it) },
        )
    }

    composable<Route.ScanCenters> {
        ScanCentersListScreen(
            centerViewModel = centerViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = { centerViewModel.searchImagingCenters() },
            onClear = { centerViewModel.listImagingCenters() },
            onError = { onError(it) },
        )
    }

    composable<Route.Pharmacies> {
        PharmaciesListScreen(
            pharmacyViewModel = pharmacyViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = { pharmacyViewModel.searchPharmacies() },
            onClear = { pharmacyViewModel.listPharmacies() },
            onError = { onError(it) },
        )
    }
}

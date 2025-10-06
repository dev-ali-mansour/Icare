package eg.edu.cu.csds.icare.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.home.screen.home.HomeScreen
import eg.edu.cu.csds.icare.home.screen.imaging.ImagingCentersListScreen
import eg.edu.cu.csds.icare.home.screen.lab.LabCenterListScreen
import eg.edu.cu.csds.icare.home.screen.pharmacy.PharmacyListScreen

fun NavGraphBuilder.homeRoute(
    selectedDoctorViewModel: SelectedDoctorViewModel,
    navigateUp: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.Home> {
        LaunchedEffect(true) {
            selectedDoctorViewModel.onSelectDoctor(null)
        }

        HomeScreen(
            navigateToRoute = { navigateToRoute(it) },
            navigateToDoctorDetails = {
                selectedDoctorViewModel.onSelectDoctor(it)
                navigateToRoute(Route.Booking)
            },
        )
    }

    composable<Route.LabCenters> {
        LabCenterListScreen(
            onNavigationIconClicked = { navigateUp() },
        )
    }

    composable<Route.ScanCenters> {
        ImagingCentersListScreen(
            onNavigationIconClicked = { navigateUp() },
        )
    }

    composable<Route.Pharmacies> {
        PharmacyListScreen(
            onNavigationIconClicked = { navigateUp() },
        )
    }
}

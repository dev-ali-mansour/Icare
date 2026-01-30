package eg.edu.cu.csds.icare.feature.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.home.screen.home.HomeScreen
import eg.edu.cu.csds.icare.feature.home.screen.imaging.ImagingCentersListScreen
import eg.edu.cu.csds.icare.feature.home.screen.lab.LabCenterListScreen
import eg.edu.cu.csds.icare.feature.home.screen.pharmacy.PharmacyListScreen

fun EntryProviderScope<NavKey>.homeEntryBuilder(
    selectedDoctorViewModel: SelectedDoctorViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    entry<Route.Home> {
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

    entry<Route.LabCenters> {
        LabCenterListScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }

    entry<Route.ScanCenters> {
        ImagingCentersListScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }

    entry<Route.Pharmacies> {
        PharmacyListScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }
}

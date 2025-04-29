package eg.edu.cu.csds.icare.home.screen.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.screen.HomeScreen

fun NavGraphBuilder.homeRoute(
    firebaseAuth: FirebaseAuth,
    mediaHelper: MediaHelper,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    clinicViewModel: ClinicViewModel,
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    appointmentViewModel: AppointmentViewModel,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.Home> {
        HomeScreen(
            firebaseAuth = firebaseAuth,
            mediaHelper = mediaHelper,
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
    }

    composable<Screen.ScanCenters> {
    }

    composable<Screen.Pharmacies> {
    }
}

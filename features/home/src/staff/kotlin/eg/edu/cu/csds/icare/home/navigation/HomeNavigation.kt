package eg.edu.cu.csds.icare.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
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
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.Home> {
        HomeScreen(
            firebaseAuth = firebaseAuth,
            mediaHelper = mediaHelper,
            mainViewModel = mainViewModel,
            homeViewModel = homeViewModel,
            clinicViewModel = clinicViewModel,
            navigateToScreen = { navigateToScreen(it) },
            onAppointmentClick = {
            },
            onSeeAllClick = {
            },
            onError = { onError(it) },
        )
    }
}

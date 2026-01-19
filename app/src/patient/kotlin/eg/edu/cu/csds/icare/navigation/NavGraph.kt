package eg.edu.cu.csds.icare.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import eg.edu.cu.csds.icare.MainActivity
import eg.edu.cu.csds.icare.core.ui.navigation.Navigator
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.activity
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.appointment.navigation.appointmentsEntryBuilder
import eg.edu.cu.csds.icare.feature.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.feature.auth.navigation.authenticationEntryBuilder
import eg.edu.cu.csds.icare.feature.consultation.navigation.consultationsEntryBuilder
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import eg.edu.cu.csds.icare.feature.home.navigation.homeEntryBuilder
import eg.edu.cu.csds.icare.feature.notification.navigation.notificationsEntryBuilder
import eg.edu.cu.csds.icare.feature.onboarding.navigation.onBoardingEntryBuilder
import eg.edu.cu.csds.icare.feature.settings.navigation.settingsEntryBuilder
import eg.edu.cu.csds.icare.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val selectedDoctorViewModel: SelectedDoctorViewModel = koinViewModel()
    val selectedAppointmentViewModel: SelectedAppointmentViewModel = koinViewModel()
    val selectedConsultationViewModel: SelectedConsultationViewModel = koinViewModel()
    val selectedPatientViewModel: SelectedPatientViewModel = koinViewModel()
    val context: Context = LocalContext.current

    NavDisplay(
        modifier = modifier,
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider =
            entryProvider {
                entry<Route.Splash> {
                    SplashScreen()
                }

                onBoardingEntryBuilder(onFinished = {
                    navigator.clearBackStack()
                    navigator.goTo(Route.SignIn)
                })

                authenticationEntryBuilder(
                    onRecoveryClicked = { navigator.goTo(Route.PasswordRecovery) },
                    onCreateAccountClicked = { navigator.goTo(Route.SignUp) },
                    onSignInClicked = {
                        navigator.clearBackStack()
                        navigator.goTo(Route.SignIn)
                    },
                    onSignInSuccess = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.activity.finish()
                        context.startActivity(intent)
                    },
                    onRecoveryCompleted = {
                        navigator.clearBackStack()
                        navigator.goTo(Route.SignIn)
                    },
                    onRegisterCompleted = {
                        navigator.clearBackStack()
                        navigator.goTo(Route.SignIn)
                    },
                )

                homeEntryBuilder(
                    selectedDoctorViewModel = selectedDoctorViewModel,
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { screen -> navigator.goTo(screen) },
                )

                notificationsEntryBuilder()

                settingsEntryBuilder(
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { navigator.goTo(it) },
                )

                appointmentsEntryBuilder(
                    selectedDoctorViewModel = selectedDoctorViewModel,
                    selectedAppointmentViewModel = selectedAppointmentViewModel,
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { navigator.goTo(it) },
                )

                consultationsEntryBuilder(
                    selectedConsultationViewModel = selectedConsultationViewModel,
                    selectedPatientViewModel = selectedPatientViewModel,
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { navigator.goTo(it) },
                )
            },
    )
}

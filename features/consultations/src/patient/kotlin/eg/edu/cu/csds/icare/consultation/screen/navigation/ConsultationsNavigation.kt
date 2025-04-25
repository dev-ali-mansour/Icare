package eg.edu.cu.csds.icare.consultation.screen.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.consultationsRoute(
    consultationViewModel: ConsultationViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.PatientMedicalRecord> {
        MedicalRecordScreen(
            consultationViewModel = consultationViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onConsultationClicked = {
                consultationViewModel.selectedConsultationState.value = it
                navigateToScreen(Screen.EditConsultation)
            },
            onError = { onError(it) },
        )
    }
}

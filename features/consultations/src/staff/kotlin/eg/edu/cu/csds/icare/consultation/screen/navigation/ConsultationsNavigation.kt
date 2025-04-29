package eg.edu.cu.csds.icare.consultation.screen.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.EditConsultationScreen
import eg.edu.cu.csds.icare.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.consultation.screen.NewConsultationScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.consultationsRoute(
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
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

    composable<Screen.NewConsultation> {
        NewConsultationScreen(
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            consultationViewModel = consultationViewModel,
            onPatientCardClick = {
                consultationViewModel.getMedicalRecord(it)
                navigateToScreen(Screen.PatientMedicalRecord)
            },
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { consultationViewModel.addNewConsultation() },
            navigateToScreen = { navigateToScreen(it) },
            onError = { onError(it) },
        )
    }

    composable<Screen.EditConsultation> {
        EditConsultationScreen(
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            consultationViewModel = consultationViewModel,
            onPatientCardClick = {
                consultationViewModel.getMedicalRecord(it)
                navigateToScreen(Screen.PatientMedicalRecord)
            },
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { consultationViewModel.updateConsultation() },
            navigateToScreen = { navigateToScreen(it) },
            onError = { onError(it) },
        )
    }
}

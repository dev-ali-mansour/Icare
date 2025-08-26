package eg.edu.cu.csds.icare.consultation.screen.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.EditConsultationScreen
import eg.edu.cu.csds.icare.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.consultation.screen.NewConsultationScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Route

fun NavGraphBuilder.consultationsRoute(
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    consultationViewModel: ConsultationViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Route) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Route.PatientMedicalRecord> {
        MedicalRecordScreen(
            consultationViewModel = consultationViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onConsultationClicked = {
                consultationViewModel.selectedConsultationState.value = it
                navigateToScreen(Route.EditConsultation)
            },
            onError = { onError(it) },
        )
    }

    composable<Route.NewConsultation> {
        NewConsultationScreen(
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            consultationViewModel = consultationViewModel,
            onPatientCardClick = {
                consultationViewModel.getMedicalRecord(it)
                navigateToScreen(Route.PatientMedicalRecord)
            },
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { consultationViewModel.addNewConsultation() },
            navigateToScreen = { navigateToScreen(it) },
            onError = { onError(it) },
        )
    }

    composable<Route.EditConsultation> {
        EditConsultationScreen(
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            consultationViewModel = consultationViewModel,
            onPatientCardClick = {
                consultationViewModel.getMedicalRecord(it)
                navigateToScreen(Route.PatientMedicalRecord)
            },
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { consultationViewModel.updateConsultation() },
            navigateToScreen = { navigateToScreen(it) },
            onError = { onError(it) },
        )
    }
}

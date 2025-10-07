package eg.edu.cu.csds.icare.feature.consultation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordEvent
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.consultationsRoute(
    selectedConsultationViewModel: SelectedConsultationViewModel,
    selectedPatientViewModel: SelectedPatientViewModel,
    navigateUp: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.PatientMedicalRecord> {
        composable<Route.PatientMedicalRecord> {
            val viewModel: MedicalRecordViewModel = koinViewModel()
            val selectedPatientId by selectedPatientViewModel.selectedPatientId
                .collectAsStateWithLifecycle()
            LaunchedEffect(selectedPatientId) {
                selectedPatientId?.let { patientId ->
                    viewModel.processEvent(MedicalRecordEvent.LoadMedicalRecord(patientId))
                }
            }
            MedicalRecordScreen(
                navigateUp = { navigateUp() },
                navigateToConsultationRoute = {
                    selectedConsultationViewModel.onSelectConsultation(it)
                    navigateToRoute(Route.UpdateConsultation)
                },
            )
        }
    }
}

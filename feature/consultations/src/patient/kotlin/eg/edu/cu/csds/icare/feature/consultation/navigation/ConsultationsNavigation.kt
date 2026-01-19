package eg.edu.cu.csds.icare.feature.consultation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordEvent
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<Any>.consultationsEntryBuilder(
    selectedConsultationViewModel: SelectedConsultationViewModel,
    selectedPatientViewModel: SelectedPatientViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    entry<Route.PatientMedicalRecord> {
        entry<Route.PatientMedicalRecord> {
            val viewModel: MedicalRecordViewModel = koinViewModel()
            val selectedPatientId by selectedPatientViewModel.selectedPatientId
                .collectAsStateWithLifecycle()
            LaunchedEffect(selectedPatientId) {
                selectedPatientId?.let { patientId ->
                    viewModel.processEvent(MedicalRecordEvent.LoadMedicalRecord(patientId))
                }
            }
            MedicalRecordScreen(
                onNavigationIconClicked = { onNavigationIconClicked() },
                navigateToConsultationRoute = {
                    selectedConsultationViewModel.onSelectConsultation(it)
                    navigateToRoute(Route.UpdateConsultation)
                },
            )
        }
    }
}

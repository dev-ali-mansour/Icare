package eg.edu.cu.csds.icare.feature.consultation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationIntent
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordIntent
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.add.NewConsultationScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.add.NewConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.update.UpdateConsultationScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.update.UpdateConsultationViewModel
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<NavKey>.consultationsEntryBuilder(
    selectedAppointmentViewModel: SelectedAppointmentViewModel,
    selectedConsultationViewModel: SelectedConsultationViewModel,
    selectedPatientViewModel: SelectedPatientViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    entry<Route.PatientMedicalRecord> {
        LaunchedEffect(true) {
            selectedConsultationViewModel.onSelectConsultation(null)
        }

        val viewModel: MedicalRecordViewModel = koinViewModel()
        val selectedPatientId by selectedPatientViewModel.selectedPatientId
            .collectAsStateWithLifecycle()
        LaunchedEffect(selectedPatientId) {
            selectedPatientId?.let { patientId ->
                viewModel.handleIntent(MedicalRecordIntent.LoadMedicalRecord(patientId))
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

    entry<Route.NewConsultation> {
        val viewModel: NewConsultationViewModel = koinViewModel()
        val selectedAppointment by selectedAppointmentViewModel.selectedAppointment
            .collectAsStateWithLifecycle()
        LaunchedEffect(selectedAppointment) {
            selectedAppointment?.let { appointment ->
                viewModel.handleIntent(ConsultationIntent.UpdateAppointment(appointment))
            }
        }
        NewConsultationScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            navigateToMedicalRecord = {
                selectedPatientViewModel.onSelectPatientId(it)
                navigateToRoute(Route.PatientMedicalRecord)
            },
        )
    }

    entry<Route.UpdateConsultation> {
        val viewModel: UpdateConsultationViewModel = koinViewModel()
        val selectedConsultation by selectedConsultationViewModel.selectedConsultation
            .collectAsStateWithLifecycle()
        LaunchedEffect(selectedConsultation) {
            selectedConsultation?.let { consultation ->
                viewModel.handleIntent(ConsultationIntent.LoadConsultation(consultation))
            }
        }

        UpdateConsultationScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            navigateToMedicalRecord = {
                selectedPatientViewModel.onSelectPatientId(it)
                navigateToRoute(Route.PatientMedicalRecord)
            },
        )
    }
}

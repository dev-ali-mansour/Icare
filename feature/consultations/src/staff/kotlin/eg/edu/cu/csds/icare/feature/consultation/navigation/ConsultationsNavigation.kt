package eg.edu.cu.csds.icare.feature.consultation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.feature.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationEvent
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordEvent
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.MedicalRecordViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.add.NewConsultationScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.add.NewConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.update.UpdateConsultationScreen
import eg.edu.cu.csds.icare.feature.consultation.screen.update.UpdateConsultationViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.consultationsRoute(
    selectedAppointmentViewModel: SelectedAppointmentViewModel,
    selectedConsultationViewModel: SelectedConsultationViewModel,
    selectedPatientViewModel: SelectedPatientViewModel,
    navigateUp: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.PatientMedicalRecord> {
        LaunchedEffect(true) {
            selectedConsultationViewModel.onSelectConsultation(null)
        }

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

    composable<Route.NewConsultation> {
        val viewModel: NewConsultationViewModel = koinViewModel()
        val selectedAppointment by selectedAppointmentViewModel.selectedAppointment
            .collectAsStateWithLifecycle()
        LaunchedEffect(selectedAppointment) {
            selectedAppointment?.let { appointment ->
                viewModel.processEvent(ConsultationEvent.UpdateAppointment(appointment))
            }
        }
        NewConsultationScreen(
            navigateUp = { navigateUp() },
            navigateToMedicalRecord = {
                selectedPatientViewModel.onSelectPatientId(it)
                navigateToRoute(Route.PatientMedicalRecord)
            },
        )
    }

    composable<Route.UpdateConsultation> {
        val viewModel: UpdateConsultationViewModel = koinViewModel()
        val selectedConsultation by selectedConsultationViewModel.selectedConsultation
            .collectAsStateWithLifecycle()
        LaunchedEffect(selectedConsultation) {
            selectedConsultation?.let { consultation ->
                viewModel.processEvent(ConsultationEvent.LoadConsultation(consultation))
            }
        }

        UpdateConsultationScreen(
            navigateUp = { navigateUp() },
            navigateToMedicalRecord = {
                selectedPatientViewModel.onSelectPatientId(it)
                navigateToRoute(Route.PatientMedicalRecord)
            },
        )
    }
}

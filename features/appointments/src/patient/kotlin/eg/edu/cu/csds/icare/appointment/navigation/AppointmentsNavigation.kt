package eg.edu.cu.csds.icare.appointment.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.appointment.screen.appointments.MyAppointmentsScreen
import eg.edu.cu.csds.icare.appointment.screen.booking.BookingEvent
import eg.edu.cu.csds.icare.appointment.screen.booking.BookingScreen
import eg.edu.cu.csds.icare.appointment.screen.booking.BookingViewModel
import eg.edu.cu.csds.icare.appointment.screen.doctors.DoctorListScreen
import eg.edu.cu.csds.icare.appointment.screen.reschedule.AppointmentRescheduleScreen
import eg.edu.cu.csds.icare.appointment.screen.reschedule.RescheduleEvent
import eg.edu.cu.csds.icare.appointment.screen.reschedule.RescheduleViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.appointmentsRoute(
    selectedDoctorViewModel: SelectedDoctorViewModel,
    selectedAppointmentViewModel: SelectedAppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.MyAppointments> {
        LaunchedEffect(true) {
            selectedAppointmentViewModel.onSelectAppointment(null)
        }

        MyAppointmentsScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            navigateToRescheduleRoute = {
                selectedAppointmentViewModel.onSelectAppointment(it)
                navigateToRoute(Route.AppointmentReschedule)
            },
        )
    }

    composable<Route.DoctorList> {
        LaunchedEffect(true) {
            selectedDoctorViewModel.onSelectDoctor(null)
        }

        DoctorListScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            navigateToBookingRoute = {
                selectedDoctorViewModel.onSelectDoctor(it)
                navigateToRoute(Route.Booking)
            },
        )
    }

    composable<Route.Booking> {
        val viewModel: BookingViewModel = koinViewModel()
        val selectedDoctor by selectedDoctorViewModel.selectedDoctor.collectAsStateWithLifecycle()
        LaunchedEffect(selectedDoctor) {
            selectedDoctor?.let { doctor ->
                viewModel.processEvent(BookingEvent.SelectDoctor(doctor))
            }
        }

        BookingScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { onNavigationIconClicked() },
        )
    }

    composable<Route.AppointmentReschedule> {
        val viewModel: RescheduleViewModel = koinViewModel()
        val selectAppointment by selectedAppointmentViewModel.selectedAppointment
            .collectAsStateWithLifecycle()
        LaunchedEffect(selectAppointment) {
            selectAppointment?.let { appointment ->
                viewModel.processEvent(RescheduleEvent.SelectAppointment(appointment))
            }
        }

        AppointmentRescheduleScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }
}

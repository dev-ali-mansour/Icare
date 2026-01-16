package eg.edu.cu.csds.icare.feature.appointment.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.feature.appointment.screen.appointments.MyAppointmentsScreen
import eg.edu.cu.csds.icare.feature.appointment.screen.booking.BookingEvent
import eg.edu.cu.csds.icare.feature.appointment.screen.booking.BookingScreen
import eg.edu.cu.csds.icare.feature.appointment.screen.booking.BookingViewModel
import eg.edu.cu.csds.icare.feature.appointment.screen.doctors.DoctorListScreen
import eg.edu.cu.csds.icare.feature.appointment.screen.reschedule.AppointmentRescheduleScreen
import eg.edu.cu.csds.icare.feature.appointment.screen.reschedule.RescheduleEvent
import eg.edu.cu.csds.icare.feature.appointment.screen.reschedule.RescheduleViewModel
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<Any>.appointmentsEntryBuilder(
    selectedDoctorViewModel: SelectedDoctorViewModel,
    selectedAppointmentViewModel: SelectedAppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    entry<Route.MyAppointments> {
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

    entry<Route.DoctorList> {
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

    entry<Route.Booking> {
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

    entry<Route.AppointmentReschedule> {
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

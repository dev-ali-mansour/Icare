package eg.edu.cu.csds.icare.admin.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.AdminScreen
import eg.edu.cu.csds.icare.admin.screen.center.CenterEvent
import eg.edu.cu.csds.icare.admin.screen.center.SelectedCenterViewModel
import eg.edu.cu.csds.icare.admin.screen.center.add.NewCenterScreen
import eg.edu.cu.csds.icare.admin.screen.center.update.UpdateCenterScreen
import eg.edu.cu.csds.icare.admin.screen.center.update.UpdateCenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicEvent
import eg.edu.cu.csds.icare.admin.screen.clinic.SelectedClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.add.NewClinicScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.update.UpdateClinicScreen
import eg.edu.cu.csds.icare.admin.screen.clinic.update.UpdateClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.clinician.ClinicianEvent
import eg.edu.cu.csds.icare.admin.screen.clinician.SelectedClinicianViewModel
import eg.edu.cu.csds.icare.admin.screen.clinician.add.NewClinicStaffScreen
import eg.edu.cu.csds.icare.admin.screen.clinician.update.UpdateClinicianScreen
import eg.edu.cu.csds.icare.admin.screen.clinician.update.UpdateClinicianViewModel
import eg.edu.cu.csds.icare.admin.screen.doctor.DoctorEvent
import eg.edu.cu.csds.icare.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.admin.screen.doctor.add.NewDoctorScreen
import eg.edu.cu.csds.icare.admin.screen.doctor.update.UpdateDoctorScreen
import eg.edu.cu.csds.icare.admin.screen.doctor.update.UpdateDoctorViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacist.PharmacistEvent
import eg.edu.cu.csds.icare.admin.screen.pharmacist.SelectedPharmacistViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacist.add.NewPharmacistScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacist.update.UpdatePharmacistScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacist.update.UpdatePharmacistViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyEvent
import eg.edu.cu.csds.icare.admin.screen.pharmacy.SelectedPharmacyViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.add.NewPharmacyScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.update.UpdatePharmacyScreen
import eg.edu.cu.csds.icare.admin.screen.pharmacy.update.UpdatePharmacyViewModel
import eg.edu.cu.csds.icare.admin.screen.staff.SelectedStaffViewModel
import eg.edu.cu.csds.icare.admin.screen.staff.StaffEvent
import eg.edu.cu.csds.icare.admin.screen.staff.add.NewStaffScreen
import eg.edu.cu.csds.icare.admin.screen.staff.update.UpdateStaffScreen
import eg.edu.cu.csds.icare.admin.screen.staff.update.UpdateStaffViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.adminRoute(
    selectedClinicViewModel: SelectedClinicViewModel,
    selectedDoctorViewModel: SelectedDoctorViewModel,
    selectedClinicianViewModel: SelectedClinicianViewModel,
    selectedPharmacyViewModel: SelectedPharmacyViewModel,
    selectedPharmacistViewModel: SelectedPharmacistViewModel,
    selectedCenterViewModel: SelectedCenterViewModel,
    selectedStaffViewModel: SelectedStaffViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.Admin> {
        LaunchedEffect(true) {
            selectedClinicViewModel.onSelectClinic(null)
            selectedDoctorViewModel.onSelectDoctor(null)
            selectedClinicianViewModel.onSelectClinician(null)
            selectedPharmacyViewModel.onSelectPharmacy(null)
            selectedPharmacistViewModel.onSelectPharmacist(null)
            selectedCenterViewModel.onSelectCenter(null)
            selectedStaffViewModel.onSelectStaff(null)
        }

        AdminScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            navigateToRoute = {
                navigateToRoute(it)
            },
            navigateToClinicDetails = { clinic ->
                selectedClinicViewModel.onSelectClinic(clinic)
                navigateToRoute(Route.UpdateClinic)
            },
            navigateToDoctorDetails = { doctor ->
                selectedDoctorViewModel.onSelectDoctor(doctor)
                navigateToRoute(Route.UpdateDoctor)
            },
            navigateToClinicianDetails = { clinician ->
                selectedClinicianViewModel.onSelectClinician(clinician)
                navigateToRoute(Route.UpdateClinician)
            },
            navigateToPharmacyDetails = { pharmacy ->
                selectedPharmacyViewModel.onSelectPharmacy(pharmacy)
                navigateToRoute(Route.UpdatePharmacy)
            },
            navigateToPharmacistDetails = { pharmacist ->
                selectedPharmacistViewModel.onSelectPharmacist(pharmacist)
                navigateToRoute(Route.UpdatePharmacist)
            },
            navigateToCenterDetails = { center ->
                selectedCenterViewModel.onSelectCenter(center)
                navigateToRoute(Route.UpdateCenter)
            },
            navigateToStaffDetails = { staff ->
                selectedStaffViewModel.onSelectStaff(staff)
                navigateToRoute(Route.UpdateStaff)
            },
        )
    }

    composable<Route.NewClinic> {
        NewClinicScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdateClinic> {
        val viewModel: UpdateClinicViewModel = koinViewModel()
        val selectedClinic by selectedClinicViewModel.selectedClinic.collectAsStateWithLifecycle()
        LaunchedEffect(selectedClinic) {
            selectedClinic?.let { clinic ->
                viewModel.processEvent(ClinicEvent.SelectClinic(clinic))
            }
        }
        UpdateClinicScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.NewDoctor> {
        NewDoctorScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdateDoctor> {
        val viewModel: UpdateDoctorViewModel = koinViewModel()
        val selectedDoctor by selectedDoctorViewModel.selectedDoctor.collectAsStateWithLifecycle()
        LaunchedEffect(selectedDoctor) {
            selectedDoctor?.let { doctor ->
                viewModel.processEvent(DoctorEvent.SelectDoctor(doctor))
            }
        }
        UpdateDoctorScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.NewClinician> {
        NewClinicStaffScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdateClinician> {
        val viewModel: UpdateClinicianViewModel = koinViewModel()
        val selectedClinician by selectedClinicianViewModel.selectedClinician.collectAsStateWithLifecycle()
        LaunchedEffect(selectedClinician) {
            selectedClinician?.let { clinician ->
                viewModel.processEvent(ClinicianEvent.SelectClinician(clinician))
            }
        }
        UpdateClinicianScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.NewPharmacy> {
        NewPharmacyScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdatePharmacy> {
        val viewModel: UpdatePharmacyViewModel = koinViewModel()
        val selectedPharmacy by selectedPharmacyViewModel.selectedPharmacy.collectAsStateWithLifecycle()
        LaunchedEffect(selectedPharmacy) {
            selectedPharmacy?.let { pharmacy ->
                viewModel.processEvent(PharmacyEvent.SelectPharmacy(pharmacy))
            }
        }
        UpdatePharmacyScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.NewPharmacist> {
        NewPharmacistScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdatePharmacist> {
        val viewModel: UpdatePharmacistViewModel = koinViewModel()
        val selectedPharmacist by selectedPharmacistViewModel.selectedPharmacist.collectAsStateWithLifecycle()
        LaunchedEffect(selectedPharmacist) {
            selectedPharmacist?.let { pharmacist ->
                viewModel.processEvent(PharmacistEvent.SelectPharmacist(pharmacist))
            }
        }

        UpdatePharmacistScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.NewCenter> {
        NewCenterScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdateCenter> {
        val viewModel: UpdateCenterViewModel = koinViewModel()
        val selectedCenter by selectedCenterViewModel.selectedCenter.collectAsStateWithLifecycle()
        LaunchedEffect(selectedCenter) {
            selectedCenter?.let { center ->
                viewModel.processEvent(CenterEvent.SelectCenter(center))
            }
        }
        UpdateCenterScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.NewStaff> {
        NewStaffScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    composable<Route.UpdateStaff> {
        val viewModel: UpdateStaffViewModel = koinViewModel()
        val selectedStaff by selectedStaffViewModel.selectedStaff.collectAsStateWithLifecycle()
        LaunchedEffect(selectedStaff) {
            selectedStaff?.let { staff ->
                viewModel.processEvent(StaffEvent.SelectStaff(staff))
            }
        }
        UpdateStaffScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }
}

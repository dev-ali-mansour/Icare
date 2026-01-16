package eg.edu.cu.csds.icare.feature.admin.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.admin.screen.AdminScreen
import eg.edu.cu.csds.icare.feature.admin.screen.center.CenterEvent
import eg.edu.cu.csds.icare.feature.admin.screen.center.SelectedCenterViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.center.add.NewCenterScreen
import eg.edu.cu.csds.icare.feature.admin.screen.center.update.UpdateCenterScreen
import eg.edu.cu.csds.icare.feature.admin.screen.center.update.UpdateCenterViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.ClinicEvent
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.SelectedClinicViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.add.NewClinicScreen
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.update.UpdateClinicScreen
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.update.UpdateClinicViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.ClinicianEvent
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.SelectedClinicianViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.add.NewClinicStaffScreen
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.update.UpdateClinicianScreen
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.update.UpdateClinicianViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorEvent
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.add.NewDoctorScreen
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.update.UpdateDoctorScreen
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.update.UpdateDoctorViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.PharmacistEvent
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.SelectedPharmacistViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.add.NewPharmacistScreen
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.update.UpdatePharmacistScreen
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.update.UpdatePharmacistViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.PharmacyEvent
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.SelectedPharmacyViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.add.NewPharmacyScreen
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.update.UpdatePharmacyScreen
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.update.UpdatePharmacyViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.staff.SelectedStaffViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.staff.StaffEvent
import eg.edu.cu.csds.icare.feature.admin.screen.staff.add.NewStaffScreen
import eg.edu.cu.csds.icare.feature.admin.screen.staff.update.UpdateStaffScreen
import eg.edu.cu.csds.icare.feature.admin.screen.staff.update.UpdateStaffViewModel
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<Any>.adminEntryBuilder(
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
    entry<Route.Admin> {
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

    entry<Route.NewClinic> {
        NewClinicScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdateClinic> {
        val viewModel: UpdateClinicViewModel = koinViewModel()
        val selectedClinic by selectedClinicViewModel.selectedClinic.collectAsStateWithLifecycle()
        LaunchedEffect(selectedClinic) {
            selectedClinic?.let { clinic ->
                viewModel.processEvent(ClinicEvent.LoadClinic(clinic))
            }
        }
        UpdateClinicScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.NewDoctor> {
        NewDoctorScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdateDoctor> {
        val viewModel: UpdateDoctorViewModel = koinViewModel()
        val selectedDoctor by selectedDoctorViewModel.selectedDoctor.collectAsStateWithLifecycle()
        LaunchedEffect(selectedDoctor) {
            selectedDoctor?.let { doctor ->
                viewModel.processEvent(DoctorEvent.LoadDoctor(doctor))
            }
        }
        UpdateDoctorScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.NewClinician> {
        NewClinicStaffScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdateClinician> {
        val viewModel: UpdateClinicianViewModel = koinViewModel()
        val selectedClinician by selectedClinicianViewModel.selectedClinician.collectAsStateWithLifecycle()
        LaunchedEffect(selectedClinician) {
            selectedClinician?.let { clinician ->
                viewModel.processEvent(ClinicianEvent.LoadClinician(clinician))
            }
        }
        UpdateClinicianScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.NewPharmacy> {
        NewPharmacyScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdatePharmacy> {
        val viewModel: UpdatePharmacyViewModel = koinViewModel()
        val selectedPharmacy by selectedPharmacyViewModel.selectedPharmacy.collectAsStateWithLifecycle()
        LaunchedEffect(selectedPharmacy) {
            selectedPharmacy?.let { pharmacy ->
                viewModel.processEvent(PharmacyEvent.LoadPharmacy(pharmacy))
            }
        }
        UpdatePharmacyScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.NewPharmacist> {
        NewPharmacistScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdatePharmacist> {
        val viewModel: UpdatePharmacistViewModel = koinViewModel()
        val selectedPharmacist by selectedPharmacistViewModel.selectedPharmacist.collectAsStateWithLifecycle()
        LaunchedEffect(selectedPharmacist) {
            selectedPharmacist?.let { pharmacist ->
                viewModel.processEvent(PharmacistEvent.LoadPharmacist(pharmacist))
            }
        }

        UpdatePharmacistScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.NewCenter> {
        NewCenterScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdateCenter> {
        val viewModel: UpdateCenterViewModel = koinViewModel()
        val selectedCenter by selectedCenterViewModel.selectedCenter.collectAsStateWithLifecycle()
        LaunchedEffect(selectedCenter) {
            selectedCenter?.let { center ->
                viewModel.processEvent(CenterEvent.LoadCenter(center))
            }
        }
        UpdateCenterScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.NewStaff> {
        NewStaffScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }

    entry<Route.UpdateStaff> {
        val viewModel: UpdateStaffViewModel = koinViewModel()
        val selectedStaff by selectedStaffViewModel.selectedStaff.collectAsStateWithLifecycle()
        LaunchedEffect(selectedStaff) {
            selectedStaff?.let { staff ->
                viewModel.processEvent(StaffEvent.LoadStaff(staff))
            }
        }
        UpdateStaffScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSuccess = { navigateToRoute(Route.Admin) },
        )
    }
}

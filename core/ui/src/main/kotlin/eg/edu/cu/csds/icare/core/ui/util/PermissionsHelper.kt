package eg.edu.cu.csds.icare.core.ui.util

import eg.edu.cu.csds.icare.core.domain.model.Permission
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun hasPermission(
    permissions: List<Short>,
    route: Screen,
): Boolean =
    when (route) {
        Screen.Admin,
        Screen.Clinics,
        Screen.NewClinic,
        Screen.EditClinic,
        Screen.Pharmacies,
        Screen.NewPharmacy,
        Screen.EditPharmacy,
        Screen.Centers,
        Screen.NewCenter,
        Screen.EditCenter,
        Screen.Doctors,
        Screen.NewDoctor,
        Screen.EditDoctor,
        Screen.ClinicStaffs,
        Screen.NewClinicStaff,
        Screen.EditClinicStaff,
        Screen.Pharmacists,
        Screen.NewPharmacist,
        Screen.EditPharmacist,
        Screen.ClinicStaffs,
        Screen.NewCenterStaff,
        Screen.EditCenterStaff,
        Screen.ViewMedicalHistory,
        -> permissions.contains(Permission.AdminPermission.code)

        Screen.CreatePrescription -> permissions.contains(Permission.CreatePrescriptionPermission.code)

        else -> false
    }

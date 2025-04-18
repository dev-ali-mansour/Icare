package eg.edu.cu.csds.icare.core.ui.util

import eg.edu.cu.csds.icare.core.domain.model.Permission
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun hasPermission(
    permissions: List<Short>,
    route: Screen,
): Boolean =
    when (route) {
        Screen.Admin,
        Screen.NewClinic,
        Screen.EditClinic,
        Screen.NewPharmacy,
        Screen.EditPharmacy,
        Screen.NewCenter,
        Screen.EditCenter,
        Screen.NewDoctor,
        Screen.EditDoctor,
        Screen.NewClinicStaff,
        Screen.EditClinicStaff,
        Screen.NewPharmacist,
        Screen.EditPharmacist,
        Screen.NewCenterStaff,
        Screen.EditCenterStaff,
        Screen.ViewMedicalHistory,
        -> permissions.contains(Permission.AdminPermission.code)

        Screen.CreatePrescription -> permissions.contains(Permission.CreatePrescriptionPermission.code)

        else -> false
    }

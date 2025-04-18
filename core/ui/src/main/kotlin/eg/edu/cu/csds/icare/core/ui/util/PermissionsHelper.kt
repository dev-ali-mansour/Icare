package eg.edu.cu.csds.icare.core.ui.util

import eg.edu.cu.csds.icare.core.domain.model.Permission
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun hasPermission(
    permissions: List<Short>,
    route: Screen,
): Boolean =
    when (route) {
        Screen.Admin,
        Screen.ViewMedicalHistory,
        -> permissions.contains(Permission.AdminPermission.code)

        Screen.CreatePrescription -> permissions.contains(Permission.CreatePrescriptionPermission.code)

        else -> false
    }

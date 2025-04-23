package eg.edu.cu.csds.icare.core.ui.util

import eg.edu.cu.csds.icare.core.ui.common.Role

fun authorize(roleId: Short): Boolean =
    isAdmin(roleId) ||
        roleId == Role.DoctorRole.code ||
        roleId == Role.ClinicStaffRole.code ||
        roleId == Role.PharmacistRole.code ||
        roleId == Role.CenterStaffRole.code

fun isAdmin(roleId: Short): Boolean = roleId == Role.AdminRole.code

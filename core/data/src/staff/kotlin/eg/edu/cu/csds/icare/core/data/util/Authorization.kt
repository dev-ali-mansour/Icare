package eg.edu.cu.csds.icare.core.data.util

fun isAuthorized(roleId: Short): Boolean =
    roleId == Role.AdminRole.code ||
        roleId == Role.DoctorRole.code ||
        roleId == Role.ClinicianRole.code ||
        roleId == Role.PharmacistRole.code ||
        roleId == Role.CenterStaffRole.code

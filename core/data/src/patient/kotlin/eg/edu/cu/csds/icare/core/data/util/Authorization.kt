package eg.edu.cu.csds.icare.core.data.util

fun isAuthorized(roleId: Short): Boolean = roleId == Role.AdminRole.code || roleId == Role.PatientRole.code

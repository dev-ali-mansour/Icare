package eg.edu.cu.csds.icare.admin.screen.clinician

import eg.edu.cu.csds.icare.core.domain.model.Clinic

data class ClinicianState(
    val isLoading: Boolean = false,
    val clinics: List<Clinic> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val clinicId: Long = 0,
    val isClinicsExpanded: Boolean = false,
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
)

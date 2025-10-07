package eg.edu.cu.csds.icare.feature.admin.screen.clinician.list

import eg.edu.cu.csds.icare.core.domain.model.Clinician

data class ClinicianListState(
    val isLoading: Boolean = false,
    val clinicians: List<Clinician> = emptyList(),
    val effect: ClinicianListEffect? = null,
)

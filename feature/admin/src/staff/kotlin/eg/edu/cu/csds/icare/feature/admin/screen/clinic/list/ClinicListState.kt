package eg.edu.cu.csds.icare.feature.admin.screen.clinic.list

import eg.edu.cu.csds.icare.core.domain.model.Clinic

data class ClinicListState(
    val isLoading: Boolean = false,
    val clinics: List<Clinic> = emptyList(),
    val effect: ClinicListEffect? = null,
)

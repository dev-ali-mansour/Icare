package eg.edu.cu.csds.icare.feature.admin.screen.doctor.list

import eg.edu.cu.csds.icare.core.domain.model.Doctor

data class DoctorListState(
    val isLoading: Boolean = false,
    val doctors: List<Doctor> = emptyList(),
    val effect: DoctorListEffect? = null,
)

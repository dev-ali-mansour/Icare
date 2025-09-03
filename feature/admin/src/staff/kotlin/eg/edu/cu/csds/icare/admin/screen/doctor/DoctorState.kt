package eg.edu.cu.csds.icare.admin.screen.doctor

import eg.edu.cu.csds.icare.core.domain.model.Clinic

data class DoctorState(
    val isLoading: Boolean = false,
    val clinics: List<Clinic> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val clinicId: Long = 0,
    val isClinicsExpanded: Boolean = false,
    val email: String = "",
    val phone: String = "",
    val speciality: String = "",
    val fromTime: Long = System.currentTimeMillis(),
    val toTime: Long = fromTime.plus(other = 5 * 60 * 60 * 1000),
    val rating: Double = 5.0,
    val ratingReadOnly: Boolean = true,
    val price: Double = 0.0,
    val profilePicture: String = "",
    val effect: DoctorEffect? = null,
)

package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class GetDoctorSchedule(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(uid: String): Flow<Resource<DoctorSchedule>> = repository.getDoctorSchedule(uid)
}

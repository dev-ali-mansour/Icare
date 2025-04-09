package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListClinicDoctors(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(clinicId: Long): Flow<Resource<List<Doctor>>> = repository.listClinicDoctors(clinicId)
}

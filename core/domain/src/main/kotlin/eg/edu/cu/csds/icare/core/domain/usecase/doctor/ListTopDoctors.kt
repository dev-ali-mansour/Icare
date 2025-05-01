package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListTopDoctors(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(): Flow<Resource<List<Doctor>>> = repository.listTopDoctors()
}

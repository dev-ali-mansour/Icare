package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class GetMedicalRecord(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(patientId: String): Flow<Resource<MedicalRecord>> = repository.getMedicalRecord(patientId)
}

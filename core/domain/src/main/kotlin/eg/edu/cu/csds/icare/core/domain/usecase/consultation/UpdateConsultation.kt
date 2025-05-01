package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class UpdateConsultation(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(consultation: Consultation): Flow<Resource<Nothing?>> = repository.updateConsultation(consultation)
}

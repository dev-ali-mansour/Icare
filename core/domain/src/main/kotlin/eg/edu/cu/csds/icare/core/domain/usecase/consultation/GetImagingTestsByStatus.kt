package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class GetImagingTestsByStatus(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(statusId: Short): Flow<Resource<List<Consultation>>> = repository.getImagingTestsByStatus(statusId)
}

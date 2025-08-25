package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class GetLabTestsByStatusUseCase(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(statusId: Short): Flow<Result<List<Consultation>, DataError.Remote>> =
        repository.getLabTestsByStatus(statusId)
}

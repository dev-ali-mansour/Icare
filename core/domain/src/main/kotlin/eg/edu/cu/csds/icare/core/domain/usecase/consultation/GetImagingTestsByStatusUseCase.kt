package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class GetImagingTestsByStatusUseCase(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(statusId: Short): Flow<RequestState<List<Consultation>, DataError.Remote>> =
        repository.getImagingTestsByStatus(statusId)
}

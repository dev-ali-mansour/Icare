package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class UpdateConsultationUseCase(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(consultation: Consultation): Flow<Result<Unit, DataError.Remote>> =
        repository.updateConsultation(consultation)
}

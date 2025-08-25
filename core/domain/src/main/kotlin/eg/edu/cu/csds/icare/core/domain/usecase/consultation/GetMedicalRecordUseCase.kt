package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class GetMedicalRecordUseCase(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(patientId: String): Flow<Result<MedicalRecord, DataError.Remote>> =
        repository.getMedicalRecord(patientId)
}

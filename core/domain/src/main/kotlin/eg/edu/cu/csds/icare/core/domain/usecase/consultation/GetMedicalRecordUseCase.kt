package eg.edu.cu.csds.icare.core.domain.usecase.consultation

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow

class GetMedicalRecordUseCase(
    private val repository: ConsultationsRepository,
) {
    operator fun invoke(patientId: String): Flow<RequestState<MedicalRecord, DataError.Remote>> =
        repository.getMedicalRecord(patientId)
}

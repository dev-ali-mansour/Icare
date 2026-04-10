package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentDoctorUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(): Flow<RequestState<Doctor, DataError.Remote>> = repository.getCurrentDoctor()
}

package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListTopDoctorsUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(): Flow<Result<List<Doctor>, DataError.Remote>> = repository.listTopDoctors()
}

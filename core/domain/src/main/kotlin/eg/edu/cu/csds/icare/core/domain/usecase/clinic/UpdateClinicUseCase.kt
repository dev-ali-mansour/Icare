package eg.edu.cu.csds.icare.core.domain.usecase.clinic

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class UpdateClinicUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(clinic: Clinic): Flow<Result<Unit, DataError.Remote>> =
        repository.updateClinic(clinic)
}

package eg.edu.cu.csds.icare.core.domain.usecase.clinic

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class AddNewClinic(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(clinic: Clinic): Flow<Resource<Nothing?>> = repository.addNewClinic(clinic)
}

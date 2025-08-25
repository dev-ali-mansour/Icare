package eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff

import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class AddNewClinicianCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(staff: Clinician): Flow<Result<Unit, DataError.Remote>> =
        repository.addNewClinician(staff)
}

package eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff

import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class UpdateClinicStaff(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(staff: ClinicStaff): Flow<Resource<Nothing?>> = repository.updateClinicStaff(staff)
}

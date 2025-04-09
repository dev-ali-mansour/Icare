package eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff

import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListClinicsStaff(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(forceUpdate: Boolean = false): Flow<Resource<List<ClinicStaff>>> = repository.listClinicsStaff(forceUpdate)
}

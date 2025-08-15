package eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff

import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListClinicStaffUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(): Flow<Result<List<ClinicStaff>, DataError.Remote>> = repository.listClinicStaff()
}

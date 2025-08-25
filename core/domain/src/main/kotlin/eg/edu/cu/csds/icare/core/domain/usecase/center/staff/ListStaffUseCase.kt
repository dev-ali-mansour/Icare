package eg.edu.cu.csds.icare.core.domain.usecase.center.staff

import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class ListStaffUseCase(
    private val repository: CentersRepository,
) {
    operator fun invoke(): Flow<Result<List<Staff>, DataError.Remote>> = repository.listCenterStaff()
}

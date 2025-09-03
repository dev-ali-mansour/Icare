package eg.edu.cu.csds.icare.core.domain.usecase.center.staff

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class AddNewStaffUseCase(
    private val repository: CentersRepository,
) {
    operator fun invoke(staff: Staff): Flow<Result<Unit, DataError.Remote>> =
        repository.addNewCenterStaff(staff)
}

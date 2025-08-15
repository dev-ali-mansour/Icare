package eg.edu.cu.csds.icare.core.domain.usecase.center.staff

import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class AddNewCenterStaffUseCase(
    private val repository: CentersRepository,
) {
    operator fun invoke(staff: CenterStaff): Flow<Result<Unit, DataError.Remote>> =
        repository.addNewCenterStaff(staff)
}

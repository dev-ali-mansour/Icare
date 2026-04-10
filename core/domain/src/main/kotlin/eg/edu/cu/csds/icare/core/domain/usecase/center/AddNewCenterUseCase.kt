package eg.edu.cu.csds.icare.core.domain.usecase.center

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class AddNewCenterUseCase(
    private val repository: CentersRepository,
) {
    operator fun invoke(center: LabImagingCenter): Flow<RequestState<Unit, DataError.Remote>> =
        repository.addNewCenter(center)
}

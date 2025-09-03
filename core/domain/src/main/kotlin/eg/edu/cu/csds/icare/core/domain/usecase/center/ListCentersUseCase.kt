package eg.edu.cu.csds.icare.core.domain.usecase.center

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class ListCentersUseCase(
    private val repository: CentersRepository,
) {
    operator fun invoke(
        forceUpdate: Boolean = false,
    ): Flow<Result<List<LabImagingCenter>, DataError.Remote>> = repository.listCenters(forceUpdate)
}

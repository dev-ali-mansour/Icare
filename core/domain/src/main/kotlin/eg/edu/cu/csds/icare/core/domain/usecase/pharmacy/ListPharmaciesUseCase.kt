package eg.edu.cu.csds.icare.core.domain.usecase.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class ListPharmaciesUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(forceUpdate: Boolean = false): Flow<Result<List<Pharmacy>, DataError.Remote>> =
        repository.listPharmacies(forceUpdate)
}

package eg.edu.cu.csds.icare.core.domain.usecase.pharmacy

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class ListPharmaciesUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(forceUpdate: Boolean = false): Flow<RequestState<List<Pharmacy>, DataError.Remote>> =
        repository.listPharmacies(forceUpdate)
}

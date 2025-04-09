package eg.edu.cu.csds.icare.core.domain.usecase.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class ListPharmacies(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(forceUpdate: Boolean = false): Flow<Resource<List<Pharmacy>>>
    = repository.listPharmacies(forceUpdate)
}

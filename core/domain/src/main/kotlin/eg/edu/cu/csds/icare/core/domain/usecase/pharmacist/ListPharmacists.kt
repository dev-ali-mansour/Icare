package eg.edu.cu.csds.icare.core.domain.usecase.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class ListPharmacists(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacyId: Long): Flow<Resource<List<Pharmacist>>> = repository.listPharmacists(pharmacyId)
}

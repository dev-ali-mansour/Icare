package eg.edu.cu.csds.icare.core.domain.usecase.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class AddNewPharmacist(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacist: Pharmacist): Flow<Resource<Nothing?>> = repository.addNewPharmacist(pharmacist)
}

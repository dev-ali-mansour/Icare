package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemotePharmaciesDataSource {
    fun fetchPharmacies(): Flow<Resource<List<PharmacyDto>>>

    fun addNewPharmacy(pharmacy: PharmacyDto): Flow<Resource<Nothing?>>

    fun updatePharmacy(pharmacy: PharmacyDto): Flow<Resource<Nothing?>>

    fun listPharmacists(): Flow<Resource<List<Pharmacist>>>

    fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>>

    fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>>
}

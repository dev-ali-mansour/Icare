package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.PharmacistDto
import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemotePharmaciesDataSource {
    fun fetchPharmacies(): Flow<Resource<List<PharmacyDto>>>

    fun addNewPharmacy(pharmacy: PharmacyDto): Flow<Resource<Nothing?>>

    fun updatePharmacy(pharmacy: PharmacyDto): Flow<Resource<Nothing?>>

    fun listPharmacists(): Flow<Resource<List<PharmacistDto>>>

    fun addNewPharmacist(pharmacist: PharmacistDto): Flow<Resource<Nothing?>>

    fun updatePharmacist(pharmacist: PharmacistDto): Flow<Resource<Nothing?>>
}

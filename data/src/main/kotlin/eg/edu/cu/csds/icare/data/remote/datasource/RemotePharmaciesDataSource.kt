package eg.edu.cu.csds.icare.data.remote.datasource

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemotePharmaciesDataSource {
    fun fetchPharmacies(): Flow<Resource<List<Pharmacy>>>

    fun addNewPharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>>

    fun updatePharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>>

    fun listPharmacists(pharmacyId: Long): Flow<Resource<List<Pharmacist>>>

    fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>>

    fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>>
}

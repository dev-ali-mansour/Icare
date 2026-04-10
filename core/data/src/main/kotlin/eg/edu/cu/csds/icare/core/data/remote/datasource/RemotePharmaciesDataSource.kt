package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.PharmacistDto
import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface RemotePharmaciesDataSource {
    fun fetchPharmacies(): Flow<RequestState<List<PharmacyDto>, DataError.Remote>>

    fun addNewPharmacy(pharmacy: PharmacyDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updatePharmacy(pharmacy: PharmacyDto): Flow<RequestState<Unit, DataError.Remote>>

    fun listPharmacists(): Flow<RequestState<List<PharmacistDto>, DataError.Remote>>

    fun addNewPharmacist(pharmacist: PharmacistDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updatePharmacist(pharmacist: PharmacistDto): Flow<RequestState<Unit, DataError.Remote>>
}

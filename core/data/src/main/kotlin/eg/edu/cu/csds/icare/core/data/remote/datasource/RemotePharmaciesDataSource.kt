package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.PharmacistDto
import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemotePharmaciesDataSource {
    fun fetchPharmacies(): Flow<Result<List<PharmacyDto>, DataError.Remote>>

    fun addNewPharmacy(pharmacy: PharmacyDto): Flow<Result<Unit, DataError.Remote>>

    fun updatePharmacy(pharmacy: PharmacyDto): Flow<Result<Unit, DataError.Remote>>

    fun listPharmacists(): Flow<Result<List<PharmacistDto>, DataError.Remote>>

    fun addNewPharmacist(pharmacist: PharmacistDto): Flow<Result<Unit, DataError.Remote>>

    fun updatePharmacist(pharmacist: PharmacistDto): Flow<Result<Unit, DataError.Remote>>
}

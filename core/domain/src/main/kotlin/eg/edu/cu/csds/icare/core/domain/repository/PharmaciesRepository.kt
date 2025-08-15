package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface PharmaciesRepository {
    fun listPharmacies(forceUpdate: Boolean): Flow<Result<List<Pharmacy>, DataError.Remote>>

    fun addNewPharmacy(pharmacy: Pharmacy): Flow<Result<Unit, DataError.Remote>>

    fun updatePharmacy(pharmacy: Pharmacy): Flow<Result<Unit, DataError.Remote>>

    fun listPharmacists(): Flow<Result<List<Pharmacist>, DataError.Remote>>

    fun addNewPharmacist(pharmacist: Pharmacist): Flow<Result<Unit, DataError.Remote>>

    fun updatePharmacist(pharmacist: Pharmacist): Flow<Result<Unit, DataError.Remote>>
}

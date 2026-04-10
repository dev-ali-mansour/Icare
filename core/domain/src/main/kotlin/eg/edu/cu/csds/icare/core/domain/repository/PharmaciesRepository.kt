package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface PharmaciesRepository {
    fun listPharmacies(forceUpdate: Boolean): Flow<RequestState<List<Pharmacy>, DataError.Remote>>

    fun addNewPharmacy(pharmacy: Pharmacy): Flow<RequestState<Unit, DataError.Remote>>

    fun updatePharmacy(pharmacy: Pharmacy): Flow<RequestState<Unit, DataError.Remote>>

    fun listPharmacists(): Flow<RequestState<List<Pharmacist>, DataError.Remote>>

    fun addNewPharmacist(pharmacist: Pharmacist): Flow<RequestState<Unit, DataError.Remote>>

    fun updatePharmacist(pharmacist: Pharmacist): Flow<RequestState<Unit, DataError.Remote>>
}

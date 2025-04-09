package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface PharmaciesRepository {
    fun listPharmacies(forceUpdate: Boolean): Flow<Resource<List<Pharmacy>>>

    fun addNewPharmacy(center: Pharmacy): Flow<Resource<Nothing?>>

    fun updatePharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>>

    fun listPharmacists(pharmacyId: Long): Flow<Resource<List<Pharmacist>>>

    fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>>

    fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>>
}

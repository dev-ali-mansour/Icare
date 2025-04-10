package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class PharmaciesRepositoryImpl : PharmaciesRepository {
    override fun listPharmacies(forceUpdate: Boolean): Flow<Resource<List<Pharmacy>>> {
        TODO("Not yet implemented")
    }

    override fun addNewPharmacy(center: Pharmacy): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updatePharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listPharmacists(pharmacyId: Long): Flow<Resource<List<Pharmacist>>> {
        TODO("Not yet implemented")
    }

    override fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}

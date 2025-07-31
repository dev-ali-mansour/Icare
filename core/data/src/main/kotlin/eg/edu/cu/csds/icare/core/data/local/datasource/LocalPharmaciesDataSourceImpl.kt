package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.dao.PharmacyDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.PharmacyEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalPharmaciesDataSourceImpl(
    private val pharmacyDao: PharmacyDao,
) : LocalPharmaciesDataSource {
    override fun persistPharmacies(pharmacies: List<PharmacyEntity>) {
        pharmacyDao.persistClinics(pharmacies)
    }

    override fun listPharmacies(): Flow<List<PharmacyEntity>> = pharmacyDao.listPharmacies()
}

package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.dao.PharmacyDao
import eg.edu.cu.csds.icare.data.local.db.entity.PharmacyEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalPharmacyDataSourceImpl(
    private val pharmacyDao: PharmacyDao,
) : LocalPharmacyDataSource {
    override fun persistClinics(pharmacies: List<PharmacyEntity>) {
        pharmacyDao.persistClinics(pharmacies)
    }

    override fun listPharmacies(): Flow<List<PharmacyEntity>> = pharmacyDao.listPharmacies()
}

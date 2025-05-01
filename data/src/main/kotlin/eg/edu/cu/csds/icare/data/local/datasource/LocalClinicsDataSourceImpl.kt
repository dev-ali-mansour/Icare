package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.dao.ClinicDao
import eg.edu.cu.csds.icare.data.local.db.entity.ClinicEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalClinicsDataSourceImpl(
    private val clinicDao: ClinicDao,
) : LocalClinicsDataSource {
    override suspend fun persistClinics(clinics: List<ClinicEntity>) {
        clinicDao.persistClinics(clinics)
    }

    override suspend fun listClinics(): Flow<List<ClinicEntity>> = clinicDao.listClinics()
}

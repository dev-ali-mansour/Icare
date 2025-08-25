package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.dao.DoctorDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalDoctorDataSourceImpl(
    private val doctorDao: DoctorDao,
) : LocalDoctorDataSource {
    override suspend fun persistDoctors(doctors: List<DoctorEntity>) {
        doctorDao.persistDoctors(doctors)
    }

    override fun listDoctors(): Flow<List<DoctorEntity>> = doctorDao.listDoctors()

    override fun listTopDoctors(): Flow<List<DoctorEntity>> = doctorDao.listTopDoctors()

    override fun listDoctors(clinicId: Long): Flow<List<DoctorEntity>> = doctorDao.listDoctors(clinicId)
}

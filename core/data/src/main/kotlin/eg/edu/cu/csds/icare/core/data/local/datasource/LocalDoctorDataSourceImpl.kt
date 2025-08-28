package eg.edu.cu.csds.icare.core.data.local.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.local.db.dao.DoctorDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalDoctorDataSourceImpl(
    private val auth: FirebaseAuth,
    private val doctorDao: DoctorDao,
) : LocalDoctorDataSource {
    override suspend fun persistDoctors(doctors: List<DoctorEntity>) {
        doctorDao.persistDoctors(doctors)
    }

    override suspend fun getCurrentDoctor(): DoctorEntity? = doctorDao.getDoctor(auth.currentUser?.uid ?: "")

    override fun listDoctors(): Flow<List<DoctorEntity>> = doctorDao.listDoctors()

    override fun listTopDoctors(): Flow<List<DoctorEntity>> = doctorDao.listTopDoctors()

    override fun listDoctors(clinicId: Long): Flow<List<DoctorEntity>> = doctorDao.listDoctors(clinicId)
}

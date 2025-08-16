package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

interface LocalDoctorDataSource {
    suspend fun persistDoctors(doctors: List<DoctorEntity>)

    fun listDoctors(): Flow<List<DoctorEntity>>

    fun listTopDoctors(): Flow<List<DoctorEntity>>

    fun listDoctors(clinicId: Long): Flow<List<DoctorEntity>>
}

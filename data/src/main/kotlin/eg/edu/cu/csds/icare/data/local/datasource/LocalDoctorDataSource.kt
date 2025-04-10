package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

interface LocalDoctorDataSource {
    fun persistDoctors(doctors: List<DoctorEntity>)

    fun listDoctors(): Flow<List<DoctorEntity>>

    fun listDoctors(clinicId: Long): Flow<List<DoctorEntity>>
}

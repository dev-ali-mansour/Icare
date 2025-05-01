package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.entity.ClinicEntity
import kotlinx.coroutines.flow.Flow

interface LocalClinicsDataSource {
    suspend fun persistClinics(clinics: List<ClinicEntity>)

    suspend fun listClinics(): Flow<List<ClinicEntity>>
}

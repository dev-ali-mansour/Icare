package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.entity.PharmacyEntity
import kotlinx.coroutines.flow.Flow

interface LocalPharmaciesDataSource {
    suspend fun persistPharmacies(pharmacies: List<PharmacyEntity>)

    fun listPharmacies(): Flow<List<PharmacyEntity>>
}

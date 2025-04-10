package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.entity.PharmacyEntity
import kotlinx.coroutines.flow.Flow

interface LocalPharmaciesDataSource {
    fun persistPharmacies(pharmacies: List<PharmacyEntity>)

    fun listPharmacies(): Flow<List<PharmacyEntity>>
}

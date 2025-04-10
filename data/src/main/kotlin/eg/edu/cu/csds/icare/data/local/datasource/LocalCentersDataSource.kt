package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.entity.CenterEntity
import kotlinx.coroutines.flow.Flow

interface LocalCentersDataSource {
    fun persistCenters(centers: List<CenterEntity>)

    fun listCenters(): Flow<List<CenterEntity>>
}

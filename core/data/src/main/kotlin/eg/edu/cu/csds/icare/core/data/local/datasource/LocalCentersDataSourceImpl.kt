package eg.edu.cu.csds.icare.core.data.local.datasource

import eg.edu.cu.csds.icare.core.data.local.db.dao.CenterDao
import eg.edu.cu.csds.icare.core.data.local.db.entity.CenterEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalCentersDataSourceImpl(
    private val centerDao: CenterDao,
) : LocalCentersDataSource {
    override fun persistCenters(centers: List<CenterEntity>) {
        centerDao.persistCenters(centers)
    }

    override fun listCenters(): Flow<List<CenterEntity>> = centerDao.listCenters()
}

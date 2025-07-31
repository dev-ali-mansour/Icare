package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteCentersDataSource {
    fun fetchCenters(): Flow<Resource<List<CenterDto>>>

    fun addNewCenter(center: CenterDto): Flow<Resource<Nothing?>>

    fun updateCenter(center: CenterDto): Flow<Resource<Nothing?>>

    fun listCenterStaff(): Flow<Resource<List<CenterStaff>>>

    fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>>

    fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>>
}

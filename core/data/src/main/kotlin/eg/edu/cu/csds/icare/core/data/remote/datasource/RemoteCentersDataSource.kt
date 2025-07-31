package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.data.dto.CenterStaffDto
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteCentersDataSource {
    fun fetchCenters(): Flow<Resource<List<CenterDto>>>

    fun addNewCenter(center: CenterDto): Flow<Resource<Nothing?>>

    fun updateCenter(center: CenterDto): Flow<Resource<Nothing?>>

    fun listCenterStaff(): Flow<Resource<List<CenterStaffDto>>>

    fun addNewCenterStaff(staff: CenterStaffDto): Flow<Resource<Nothing?>>

    fun updateCenterStaff(staff: CenterStaffDto): Flow<Resource<Nothing?>>
}

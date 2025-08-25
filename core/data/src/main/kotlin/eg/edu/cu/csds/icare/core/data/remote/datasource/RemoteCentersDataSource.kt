package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.data.dto.CenterStaffDto
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemoteCentersDataSource {
    fun fetchCenters(): Flow<Result<List<CenterDto>, DataError.Remote>>

    fun addNewCenter(center: CenterDto): Flow<Result<Unit, DataError.Remote>>

    fun updateCenter(center: CenterDto): Flow<Result<Unit, DataError.Remote>>

    fun listCenterStaff(): Flow<Result<List<CenterStaffDto>, DataError.Remote>>

    fun addNewCenterStaff(staff: CenterStaffDto): Flow<Result<Unit, DataError.Remote>>

    fun updateCenterStaff(staff: CenterStaffDto): Flow<Result<Unit, DataError.Remote>>
}

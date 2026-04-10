package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.data.dto.CenterStaffDto
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface RemoteCentersDataSource {
    fun fetchCenters(): Flow<RequestState<List<CenterDto>, DataError.Remote>>

    fun addNewCenter(center: CenterDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updateCenter(center: CenterDto): Flow<RequestState<Unit, DataError.Remote>>

    fun listCenterStaff(): Flow<RequestState<List<CenterStaffDto>, DataError.Remote>>

    fun addNewCenterStaff(staff: CenterStaffDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updateCenterStaff(staff: CenterStaffDto): Flow<RequestState<Unit, DataError.Remote>>
}

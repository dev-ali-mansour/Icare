package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.model.Staff
import kotlinx.coroutines.flow.Flow

interface CentersRepository {
    fun listCenters(forceUpdate: Boolean): Flow<RequestState<List<LabImagingCenter>, DataError.Remote>>

    fun addNewCenter(center: LabImagingCenter): Flow<RequestState<Unit, DataError.Remote>>

    fun updateCenter(center: LabImagingCenter): Flow<RequestState<Unit, DataError.Remote>>

    fun listCenterStaff(): Flow<RequestState<List<Staff>, DataError.Remote>>

    fun addNewCenterStaff(staff: Staff): Flow<RequestState<Unit, DataError.Remote>>

    fun updateCenterStaff(staff: Staff): Flow<RequestState<Unit, DataError.Remote>>
}

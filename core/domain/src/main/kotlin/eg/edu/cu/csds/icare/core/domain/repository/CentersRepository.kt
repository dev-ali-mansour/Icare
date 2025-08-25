package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface CentersRepository {
    fun listCenters(forceUpdate: Boolean): Flow<Result<List<LabImagingCenter>, DataError.Remote>>

    fun addNewCenter(center: LabImagingCenter): Flow<Result<Unit, DataError.Remote>>

    fun updateCenter(center: LabImagingCenter): Flow<Result<Unit, DataError.Remote>>

    fun listCenterStaff(): Flow<Result<List<Staff>, DataError.Remote>>

    fun addNewCenterStaff(staff: Staff): Flow<Result<Unit, DataError.Remote>>

    fun updateCenterStaff(staff: Staff): Flow<Result<Unit, DataError.Remote>>
}

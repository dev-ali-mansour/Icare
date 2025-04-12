package eg.edu.cu.csds.icare.data.remote.datasource

import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteCentersDataSource {
    fun fetchCenters(): Flow<Resource<List<LabImagingCenter>>>

    fun addNewCenter(center: LabImagingCenter): Flow<Resource<Nothing?>>

    fun updateCenter(center: LabImagingCenter): Flow<Resource<Nothing?>>

    fun listCenterStaff(centerId: Long): Flow<Resource<List<CenterStaff>>>

    fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>>

    fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>>
}

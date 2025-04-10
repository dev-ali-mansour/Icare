package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class CentersRepositoryImpl : CentersRepository {
    override fun listCenters(forceUpdate: Boolean): Flow<Resource<List<LabImagingCenter>>> {
        TODO("Not yet implemented")
    }

    override fun addNewCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listCenterStaff(centerId: Long): Flow<Resource<List<CenterStaff>>> {
        TODO("Not yet implemented")
    }

    override fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}

package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalCentersDataSource
import eg.edu.cu.csds.icare.data.local.db.entity.toEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toModel
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteCentersDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class CentersRepositoryImpl(
    private val remoteCentersDataSource: RemoteCentersDataSource,
    private val localCentersDataSource: LocalCentersDataSource,
) : CentersRepository {
    override fun listCenters(forceRefresh: Boolean): Flow<Resource<List<LabImagingCenter>>> =
        flow {
            if (!forceRefresh) {
                localCentersDataSource
                    .listCenters()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toModel() }))
                    }
                return@flow
            }
            remoteCentersDataSource.fetchCenters().map { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { centers ->
                            localCentersDataSource.persistCenters(centers.map { it.toEntity() })
                        }
                        localCentersDataSource
                            .listCenters()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toModel() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> = remoteCentersDataSource.addNewCenter(center)

    override fun updateCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> = remoteCentersDataSource.updateCenter(center)

    override fun listCenterStaff(centerId: Long): Flow<Resource<List<CenterStaff>>> = remoteCentersDataSource.listCenterStaff(centerId)

    override fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> = remoteCentersDataSource.addNewCenterStaff(staff)

    override fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> = remoteCentersDataSource.updateCenterStaff(staff)
}

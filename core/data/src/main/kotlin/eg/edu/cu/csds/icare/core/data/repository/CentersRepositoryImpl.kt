package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.local.datasource.LocalCentersDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toCenterDto
import eg.edu.cu.csds.icare.core.data.mappers.toCenterEntity
import eg.edu.cu.csds.icare.core.data.mappers.toCenterStaff
import eg.edu.cu.csds.icare.core.data.mappers.toCenterStaffDto
import eg.edu.cu.csds.icare.core.data.mappers.toLabImagingCenter
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteCentersDataSource
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class CentersRepositoryImpl(
    private val remoteCentersDataSource: RemoteCentersDataSource,
    private val localCentersDataSource: LocalCentersDataSource,
) : CentersRepository {
    override fun listCenters(forceUpdate: Boolean): Flow<Resource<List<LabImagingCenter>>> =
        flow {
            if (!forceUpdate) {
                localCentersDataSource
                    .listCenters()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toLabImagingCenter() }))
                    }
                return@flow
            }
            remoteCentersDataSource.fetchCenters().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { centers ->
                            localCentersDataSource.persistCenters(centers.map { it.toCenterEntity() })
                        }
                        localCentersDataSource
                            .listCenters()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toLabImagingCenter() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remoteCentersDataSource.addNewCenter(center.toCenterDto()).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listCenters(true).collect { refreshResult ->
                            when (refreshResult) {
                                is Resource.Success -> emit(Resource.Success(null))
                                is Resource.Error -> emit(Resource.Error(refreshResult.error))
                                else -> {}
                            }
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun updateCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remoteCentersDataSource.updateCenter(center.toCenterDto()).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listCenters(true).collect { refreshResult ->
                            when (refreshResult) {
                                is Resource.Success -> emit(Resource.Success(null))
                                is Resource.Error -> emit(Resource.Error(refreshResult.error))
                                else -> {}
                            }
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun listCenterStaff(): Flow<Resource<List<CenterStaff>>> =
        flow {
            remoteCentersDataSource.listCenterStaff().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toCenterStaff() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> =
        remoteCentersDataSource.addNewCenterStaff(
            staff.toCenterStaffDto(),
        )

    override fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> =
        remoteCentersDataSource.updateCenterStaff(
            staff.toCenterStaffDto(),
        )
}

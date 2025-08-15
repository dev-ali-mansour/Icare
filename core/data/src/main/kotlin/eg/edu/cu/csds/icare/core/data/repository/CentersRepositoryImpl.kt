package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.local.datasource.LocalCentersDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toCenterDto
import eg.edu.cu.csds.icare.core.data.mappers.toCenterEntity
import eg.edu.cu.csds.icare.core.data.mappers.toCenterStaff
import eg.edu.cu.csds.icare.core.data.mappers.toCenterStaffDto
import eg.edu.cu.csds.icare.core.data.mappers.toLabImagingCenter
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteCentersDataSource
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
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
    override fun listCenters(forceUpdate: Boolean): Flow<Result<List<LabImagingCenter>, DataError.Remote>> =
        flow {
            if (!forceUpdate) {
                localCentersDataSource
                    .listCenters()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Result.Success(data = entities.map { it.toLabImagingCenter() }))
                    }
                return@flow
            }
            remoteCentersDataSource.fetchCenters().collect { result ->
                result
                    .onSuccess { centers ->
                        localCentersDataSource.persistCenters(centers.map { it.toCenterEntity() })

                        localCentersDataSource
                            .listCenters()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Result.Success(data = entities.map { it.toLabImagingCenter() }))
                            }
                    }.onError { emit(Result.Error(it)) }
            }
        }

    override fun addNewCenter(center: LabImagingCenter): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remoteCentersDataSource
                .addNewCenter(center.toCenterDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listCenters(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun updateCenter(center: LabImagingCenter): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remoteCentersDataSource
                .updateCenter(center.toCenterDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listCenters(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun listCenterStaff(): Flow<Result<List<CenterStaff>, DataError.Remote>> =
        flow {
            remoteCentersDataSource
                .listCenterStaff()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(Result.Success(data = entities.map { it.toCenterStaff() }))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun addNewCenterStaff(staff: CenterStaff): Flow<Result<Unit, DataError.Remote>> =
        remoteCentersDataSource.addNewCenterStaff(staff.toCenterStaffDto())

    override fun updateCenterStaff(staff: CenterStaff): Flow<Result<Unit, DataError.Remote>> =
        remoteCentersDataSource.updateCenterStaff(staff.toCenterStaffDto())
}

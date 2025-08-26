package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.local.datasource.LocalPharmaciesDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacist
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacistDto
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacy
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacyDto
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacyEntity
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemotePharmaciesDataSource
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class PharmaciesRepositoryImpl(
    private val remotePharmaciesDataSource: RemotePharmaciesDataSource,
    private val localPharmaciesDataSource: LocalPharmaciesDataSource,
) : PharmaciesRepository {
    override fun listPharmacies(forceUpdate: Boolean): Flow<Result<List<Pharmacy>, DataError.Remote>> =
        flow {
            if (!forceUpdate) {
                localPharmaciesDataSource
                    .listPharmacies()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Result.Success(data = entities.map { it.toPharmacy() }))
                    }
                return@flow
            }
            remotePharmaciesDataSource.fetchPharmacies().collect { result ->
                result
                    .onSuccess { pharmacies ->
                        localPharmaciesDataSource.persistPharmacies(pharmacies.map { it.toPharmacyEntity() })

                        localPharmaciesDataSource
                            .listPharmacies()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Result.Success(data = entities.map { it.toPharmacy() }))
                            }
                    }.onError {
                        emit(Result.Error(it))
                    }
            }
        }

    override fun addNewPharmacy(pharmacy: Pharmacy): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remotePharmaciesDataSource
                .addNewPharmacy(pharmacy.toPharmacyDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listPharmacies(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun updatePharmacy(pharmacy: Pharmacy): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remotePharmaciesDataSource
                .updatePharmacy(pharmacy.toPharmacyDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listPharmacies(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun listPharmacists(): Flow<Result<List<Pharmacist>, DataError.Remote>> =
        flow {
            remotePharmaciesDataSource
                .listPharmacists()
                .collect { result ->
                    result
                        .onSuccess {
                            emit(Result.Success(data = it.map { staffDto -> staffDto.toPharmacist() }))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun addNewPharmacist(pharmacist: Pharmacist): Flow<Result<Unit, DataError.Remote>> =
        remotePharmaciesDataSource.addNewPharmacist(pharmacist.toPharmacistDto())

    override fun updatePharmacist(pharmacist: Pharmacist): Flow<Result<Unit, DataError.Remote>> =
        remotePharmaciesDataSource.updatePharmacist(pharmacist.toPharmacistDto())
}

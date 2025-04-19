package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalPharmaciesDataSource
import eg.edu.cu.csds.icare.data.local.db.entity.toEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toModel
import eg.edu.cu.csds.icare.data.remote.datasource.RemotePharmaciesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class PharmaciesRepositoryImpl(
    private val remotePharmaciesDataSource: RemotePharmaciesDataSource,
    private val localPharmaciesDataSource: LocalPharmaciesDataSource,
) : PharmaciesRepository {
    override fun listPharmacies(forceRefresh: Boolean): Flow<Resource<List<Pharmacy>>> =
        flow {
            if (!forceRefresh) {
                localPharmaciesDataSource
                    .listPharmacies()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toModel() }))
                    }
                return@flow
            }
            remotePharmaciesDataSource.fetchPharmacies().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { pharmacies ->
                            localPharmaciesDataSource.persistPharmacies(pharmacies.map { it.toEntity() })
                        }
                        localPharmaciesDataSource
                            .listPharmacies()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toModel() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewPharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remotePharmaciesDataSource.addNewPharmacy(pharmacy).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listPharmacies(true).collect { refreshResult ->
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

    override fun updatePharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remotePharmaciesDataSource.updatePharmacy(pharmacy).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listPharmacies(true).collect { refreshResult ->
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

    override fun listPharmacists(pharmacyId: Long): Flow<Resource<List<Pharmacist>>> =
        remotePharmaciesDataSource.listPharmacists(pharmacyId)

    override fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> =
        remotePharmaciesDataSource.addNewPharmacist(pharmacist)

    override fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> =
        remotePharmaciesDataSource.updatePharmacist(pharmacist)
}

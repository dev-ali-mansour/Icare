package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.local.datasource.LocalPharmaciesDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacy
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacyDto
import eg.edu.cu.csds.icare.core.data.mappers.toPharmacyEntity
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemotePharmaciesDataSource
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
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
    override fun listPharmacies(forceUpdate: Boolean): Flow<Resource<List<Pharmacy>>> =
        flow {
            if (!forceUpdate) {
                localPharmaciesDataSource
                    .listPharmacies()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toPharmacy() }))
                    }
                return@flow
            }
            remotePharmaciesDataSource.fetchPharmacies().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { pharmacies ->
                            localPharmaciesDataSource.persistPharmacies(pharmacies.map { it.toPharmacyEntity() })
                        }
                        localPharmaciesDataSource
                            .listPharmacies()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toPharmacy() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewPharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remotePharmaciesDataSource.addNewPharmacy(pharmacy.toPharmacyDto()).collect { res ->
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
            remotePharmaciesDataSource.updatePharmacy(pharmacy.toPharmacyDto()).collect { res ->
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

    override fun listPharmacists(): Flow<Resource<List<Pharmacist>>> = remotePharmaciesDataSource.listPharmacists()

    override fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> =
        remotePharmaciesDataSource.addNewPharmacist(pharmacist)

    override fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> =
        remotePharmaciesDataSource.updatePharmacist(pharmacist)
}

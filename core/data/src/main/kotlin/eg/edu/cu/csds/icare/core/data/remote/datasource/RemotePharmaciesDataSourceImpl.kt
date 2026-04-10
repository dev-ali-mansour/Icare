package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.PharmacistDto
import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.data.mappers.toRemoteError
import eg.edu.cu.csds.icare.core.data.remote.serivce.ApiService
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

@Single
class RemotePharmaciesDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemotePharmaciesDataSource {
    override fun fetchPharmacies(): Flow<RequestState<List<PharmacyDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    val response = service.fetchPharmacies(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(res.pharmacies))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR -> {
                                        emit(RequestState.Error(DataError.Remote.SERVER))
                                    }

                                    else -> {
                                        emit(RequestState.Error(DataError.Remote.UNKNOWN))
                                    }
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                        }

                        else -> {
                            emit(RequestState.Error(DataError.Remote.UNKNOWN))
                        }
                    }
                } ?: run {
                emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("fetchPharmacies() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewPharmacy(pharmacy: PharmacyDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertPharmacy(pharmacy.copy(token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR -> {
                                        emit(RequestState.Error(DataError.Remote.SERVER))
                                    }

                                    else -> {
                                        emit(RequestState.Error(DataError.Remote.UNKNOWN))
                                    }
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                        }

                        else -> {
                            emit(RequestState.Error(DataError.Remote.UNKNOWN))
                        }
                    }
                } ?: run {
                emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("addNewPharmacy() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updatePharmacy(pharmacy: PharmacyDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertPharmacy(pharmacy.copy(token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR -> {
                                        emit(RequestState.Error(DataError.Remote.SERVER))
                                    }

                                    else -> {
                                        emit(RequestState.Error(DataError.Remote.UNKNOWN))
                                    }
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                        }

                        else -> {
                            emit(RequestState.Error(DataError.Remote.UNKNOWN))
                        }
                    }
                } ?: run {
                emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("updatePharmacy() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun listPharmacists(): Flow<RequestState<List<PharmacistDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    val response = service.listPharmacists(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(res.pharmacists))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR -> {
                                        emit(RequestState.Error(DataError.Remote.SERVER))
                                    }

                                    else -> {
                                        emit(RequestState.Error(DataError.Remote.UNKNOWN))
                                    }
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                        }

                        else -> {
                            emit(RequestState.Error(DataError.Remote.UNKNOWN))
                        }
                    }
                }
        }.catch {
            Timber.e("listPharmacists() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewPharmacist(pharmacist: PharmacistDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertPharmacist(pharmacist.copy(token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR -> {
                                        emit(RequestState.Error(DataError.Remote.SERVER))
                                    }

                                    else -> {
                                        emit(RequestState.Error(DataError.Remote.UNKNOWN))
                                    }
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                        }

                        else -> {
                            emit(RequestState.Error(DataError.Remote.UNKNOWN))
                        }
                    }
                }
        }.catch {
            Timber.e("addNewPharmacist() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updatePharmacist(pharmacist: PharmacistDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertPharmacist(pharmacist.copy(token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))
                                    }

                                    Constants.ERROR_CODE_SERVER_ERROR -> {
                                        emit(RequestState.Error(DataError.Remote.SERVER))
                                    }

                                    else -> {
                                        emit(RequestState.Error(DataError.Remote.UNKNOWN))
                                    }
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                        }

                        else -> {
                            emit(RequestState.Error(DataError.Remote.UNKNOWN))
                        }
                    }
                }
        }.catch {
            Timber.e("updatePharmacist() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }
}

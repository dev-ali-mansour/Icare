package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.PharmacistDto
import eg.edu.cu.csds.icare.core.data.dto.PharmacyDto
import eg.edu.cu.csds.icare.core.data.mappers.toRemoteError
import eg.edu.cu.csds.icare.core.data.remote.serivce.ApiService
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
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
    override fun fetchPharmacies(): Flow<Result<List<PharmacyDto>, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.pharmacies))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("fetchPharmacies() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun addNewPharmacy(pharmacy: PharmacyDto): Flow<Result<Unit, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("addNewPharmacy() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun updatePharmacy(pharmacy: PharmacyDto): Flow<Result<Unit, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("updatePharmacy() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun listPharmacists(): Flow<Result<List<PharmacistDto>, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.pharmacists))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("listPharmacists() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun addNewPharmacist(pharmacist: PharmacistDto): Flow<Result<Unit, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("addNewPharmacist() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun updatePharmacist(pharmacist: PharmacistDto): Flow<Result<Unit, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("updatePharmacist() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }
}

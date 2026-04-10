package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.data.dto.CenterStaffDto
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
class RemoteCentersDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteCentersDataSource {
    override fun fetchCenters(): Flow<RequestState<List<CenterDto>, DataError.Remote>> =
        flow {
            auth.currentUser?.let {
                auth.currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        val response = service.fetchCenters(map)
                        when (response.code()) {
                            HTTP_OK -> {
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK -> {
                                            emit(RequestState.Success(res.centers))
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

                            else -> {
                                emit(RequestState.Error(DataError.Remote.UNKNOWN))
                            }
                        }
                    } ?: run {
                    emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                }
            }
        }.catch {
            Timber.e("fetchCenters() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewCenter(center: CenterDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertCenter(center.copy(token = token))
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
            Timber.e("addNewCenter() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updateCenter(center: CenterDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertCenter(center.copy(token = token))
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
            Timber.e("updateCenter() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun listCenterStaff(): Flow<RequestState<List<CenterStaffDto>, DataError.Remote>> =
        flow {
            auth.currentUser?.let {
                auth.currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        val response = service.listCenterStaff(map)
                        when (response.code()) {
                            HTTP_OK -> {
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK -> {
                                            emit(RequestState.Success(res.staffList))
                                        }

                                        Constants.ERROR_CODE_SERVER_ERROR -> {
                                            emit(RequestState.Error(DataError.Remote.SERVER))
                                        }
                                    }
                                }
                            }

                            else -> {
                                emit(RequestState.Error(DataError.Remote.UNKNOWN))
                            }
                        }
                    }
            }
        }.catch {
            Timber.e("listCenterStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewCenterStaff(staff: CenterStaffDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertCenterStaff(staff.copy(token = token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
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
            Timber.e("addNewCenterStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updateCenterStaff(staff: CenterStaffDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertCenterStaff(staff.copy(token = token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(Unit))
                                    }

                                    Constants.ERROR_CODE_EXPIRED_TOKEN -> {
                                        emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
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
            Timber.e("updateCenterStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }
}

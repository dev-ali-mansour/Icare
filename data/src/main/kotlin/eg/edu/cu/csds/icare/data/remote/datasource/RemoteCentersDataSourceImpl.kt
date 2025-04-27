package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthorizedException
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.data.remote.serivce.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

@Single
class RemoteCentersDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteCentersDataSource {
    override fun fetchCenters(): Flow<Resource<List<LabImagingCenter>>> =
        flow {
            emit(Resource.Loading())
            auth.currentUser?.let {
                val token =
                    runBlocking {
                        auth.currentUser
                            ?.getIdToken(false)
                            ?.await()
                            ?.token
                            .toString()
                    }
                val map = HashMap<String, String>()
                map["token"] = token
                val response = service.fetchCenters(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.centers))

                                Constants.ERROR_CODE_SERVER_ERROR ->
                                    emit(Resource.Error(ConnectException()))
                            }
                        }
                    }

                    else -> emit(Resource.Error(ConnectException(response.code().toString())))
                }
            }
        }.catch {
            Timber.e("fetchCenters() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun addNewCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }
            val response = service.addNewCenter(center.copy(token = token))
            when (response.code()) {
                HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.statusCode) {
                            Constants.ERROR_CODE_OK -> emit(Resource.Success(null))

                            Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                emit(Resource.Error(UserNotAuthenticatedException()))

                            else -> emit(Resource.Error(ConnectException()))
                        }
                    } ?: run { emit(Resource.Error(ConnectException())) }

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(Resource.Error(UserNotAuthorizedException()))

                else -> emit(Resource.Error(ConnectException(response.code().toString())))
            }
        }.catch {
            Timber.e("addNewCenter() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updateCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }
            val response = service.updateCenter(center.copy(token = token))
            when (response.code()) {
                HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.statusCode) {
                            Constants.ERROR_CODE_OK -> emit(Resource.Success(null))

                            Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                emit(Resource.Error(UserNotAuthenticatedException()))

                            else -> emit(Resource.Error(ConnectException()))
                        }
                    } ?: run { emit(Resource.Error(ConnectException())) }

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(Resource.Error(UserNotAuthorizedException()))

                else -> emit(Resource.Error(ConnectException(response.code().toString())))
            }
        }.catch {
            Timber.e("updateCenter() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun listCenterStaff(centerId: Long): Flow<Resource<List<CenterStaff>>> =
        flow {
            emit(Resource.Loading())
            auth.currentUser?.let {
                val map = HashMap<String, String>()
                map["centerId"] = centerId.toString()
                val response = service.listCenterStaff(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.staffList))

                                Constants.ERROR_CODE_SERVER_ERROR ->
                                    emit(Resource.Error(ConnectException()))
                            }
                        }
                    }

                    else -> emit(Resource.Error(ConnectException(response.code().toString())))
                }
            }
        }.catch {
            Timber.e("listCenterStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }
            val response = service.addNewCenterStaff(staff.copy(token = token))
            when (response.code()) {
                HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.statusCode) {
                            Constants.ERROR_CODE_OK -> emit(Resource.Success(null))

                            Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                emit(Resource.Error(UserNotAuthenticatedException()))

                            else -> emit(Resource.Error(ConnectException()))
                        }
                    } ?: run { emit(Resource.Error(ConnectException())) }

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(Resource.Error(UserNotAuthorizedException()))

                else -> emit(Resource.Error(ConnectException(response.code().toString())))
            }
        }.catch {
            Timber.e("addNewCenterStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }
            val response = service.updateCenterStaff(staff.copy(token = token))
            when (response.code()) {
                HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.statusCode) {
                            Constants.ERROR_CODE_OK -> emit(Resource.Success(null))

                            Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                emit(Resource.Error(UserNotAuthenticatedException()))

                            else -> emit(Resource.Error(ConnectException()))
                        }
                    } ?: run { emit(Resource.Error(ConnectException())) }

                HttpURLConnection.HTTP_UNAUTHORIZED ->
                    emit(Resource.Error(UserNotAuthorizedException()))

                else -> emit(Resource.Error(ConnectException(response.code().toString())))
            }
        }.catch {
            Timber.e("updateCenterStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }
}

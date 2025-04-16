package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthorizedException
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.data.remote.serivce.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

@Single
class RemoteClinicsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteClinicsDataSource {
    override fun fetchClinics(): Flow<Resource<List<Clinic>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let {
                    val response = service.fetchClinics()
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.clinics))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Resource.Error(UserNotAuthenticatedException()))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("fetchClinics() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.addNewClinic(clinic)
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
            Timber.e("addNewClinic() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.updateClinic(clinic)
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
            Timber.e("updateClinic() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun fetchDoctors(): Flow<Resource<List<Doctor>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let {
                    val response = service.fetchDoctors()
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.doctors))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("fetchDoctors() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.addNewDoctor(doctor)
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
            Timber.e("addNewDoctor() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.updateDoctor(doctor)
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
            Timber.e("updateDoctor() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun listClinicStaff(clinicId: Long): Flow<Resource<List<ClinicStaff>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let {
                    val map = HashMap<String, String>()
                    map["clinicId"] = clinicId.toString()
                    val response = service.listClinicStaff(map)
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
            }.onFailure {
                Timber.e("listClinicStaff() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.addNewClinicStaff(staff)
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
            Timber.e("addNewClinicStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.updateClinicStaff(staff)
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
            Timber.e("updateClinicStaff() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }
}

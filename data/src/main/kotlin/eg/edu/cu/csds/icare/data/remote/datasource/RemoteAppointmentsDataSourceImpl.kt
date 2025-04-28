package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.data.remote.serivce.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_OK

@Single
class RemoteAppointmentsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteAppointmentsDataSource {
    override fun getPatientAppointments(): Flow<Resource<List<Appointment>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
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
                    val response = service.getPatientAppointments(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.appointments))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("getPatientAppointments() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun getAppointments(): Flow<Resource<List<Appointment>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
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
                    val response = service.getAppointments(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.appointments))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("getAppointments() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val map = HashMap<String, String>()
                    map["statusId"] = statusId.toString()
                    val response = service.getAppointmentsByStatus(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.appointments))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("getAppointmentsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun bookAppointment(appointment: Appointment): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val token =
                        runBlocking {
                            auth.currentUser
                                ?.getIdToken(false)
                                ?.await()
                                ?.token
                                .toString()
                        }
                    val response = service.bookAppointment(appointment.copy(token = token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(null))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("bookAppointment() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun updateAppointment(appointment: Appointment): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val token =
                        runBlocking {
                            auth.currentUser
                                ?.getIdToken(false)
                                ?.await()
                                ?.token
                                .toString()
                        }
                    val response = service.updateAppointment(appointment.copy(token = token))
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(null))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("updateAppointment() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }
}

package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.data.remote.serivce.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
                    val response = service.getPatientAppointments()
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
                    val response = service.getAppointments()
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
                    }
                }
            }.onFailure {
                Timber.e("getAppointmentsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun bookAppointment(
        doctorId: Int,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val map = HashMap<String, String>()
                    map["doctorId"] = doctorId.toString()
                    map["dateTime"] = dateTime.toString()
                    val response = service.getAppointmentsByStatus(map)
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
                    }
                }
            }.onFailure {
                Timber.e("bookAppointment() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun updateAppointmentStatus(
        appointmentId: Long,
        statusId: Short,
    ): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val map = HashMap<String, String>()
                    map["appointmentId"] = appointmentId.toString()
                    map["statusId"] = statusId.toString()
                    val response = service.getAppointmentsByStatus(map)
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
                    }
                }
            }.onFailure {
                Timber.e("updateAppointmentStatus() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun rescheduleAppointment(
        appointmentId: Long,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let { user ->
                    val map = HashMap<String, String>()
                    map["appointmentId"] = appointmentId.toString()
                    map["dateTime"] = dateTime.toString()
                    val response = service.getAppointmentsByStatus(map)
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
                    }
                }
            }.onFailure {
                Timber.e("rescheduleAppointment() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }
}

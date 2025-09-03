package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.AdminStatisticsDto
import eg.edu.cu.csds.icare.core.data.dto.AppointmentDto
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
class RemoteAppointmentsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteAppointmentsDataSource {
    override fun getPatientAppointments(): Flow<Result<List<AppointmentDto>, DataError.Remote>> =
        flow {
            auth.currentUser?.let { user ->
                auth.currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        val response = service.getPatientAppointments(map)
                        when (response.code()) {
                            HTTP_OK ->
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK -> emit(Result.Success(res.appointments))

                                        Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                                        Constants.ERROR_CODE_SERVER_ERROR ->
                                            emit(Result.Error(DataError.Remote.SERVER))

                                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                    }
                                }

                            HttpURLConnection.HTTP_UNAUTHORIZED ->
                                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                            else ->
                                emit(Result.Error(DataError.Remote.UNKNOWN))
                        }
                    }
            }
        }.catch {
            Timber.e("getPatientAppointments() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getAppointments(): Flow<Result<List<AppointmentDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    val response = service.getAppointments(map)
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(
                                            Result.Success(res.appointments),
                                        )

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else ->
                            emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("getAppointments() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getAppointments(statusId: Short): Flow<Result<List<AppointmentDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["statusId"] = statusId.toString()
                    val response = service.getAppointmentsByStatus(map)
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(
                                            Result.Success(res.appointments),
                                        )

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else ->
                            emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("getAppointmentsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun bookAppointment(
        doctorId: String,
        dateTime: Long,
    ): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser?.let { currentUser ->
                currentUser
                    .getIdToken(false)
                    .await()
                    ?.token
                    ?.let { token ->
                        val appointment =
                            AppointmentDto(
                                token = token,
                                patientId = currentUser.uid,
                                doctorId = doctorId,
                                dateTime = dateTime,
                                statusId = 1,
                            )
                        val response =
                            service.bookAppointment(appointment)
                        when (response.code()) {
                            HTTP_OK ->
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK ->
                                            emit(
                                                Result.Success(Unit),
                                            )

                                        Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                                        Constants.ERROR_CODE_SERVER_ERROR ->
                                            emit(Result.Error(DataError.Remote.SERVER))

                                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                    }
                                }

                            HttpURLConnection.HTTP_UNAUTHORIZED ->
                                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                            else ->
                                emit(Result.Error(DataError.Remote.UNKNOWN))
                        }
                    }
            } ?: run { emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED)) }
        }.catch {
            Timber.e("bookAppointment() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun updateAppointment(appointment: AppointmentDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.updateAppointment(appointment.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(
                                            Result.Success(Unit),
                                        )

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else ->
                            emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run { emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED)) }
        }.catch {
            Timber.e("updateAppointment() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getAdminStatistics(): Flow<Result<AdminStatisticsDto, DataError.Remote>> =
        flow {
            auth.currentUser?.let { user ->
                auth.currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        val response = service.getAdminStatistics(map)
                        when (response.code()) {
                            HTTP_OK ->
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK -> emit(Result.Success(res.stats))

                                        Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                                        Constants.ERROR_CODE_SERVER_ERROR ->
                                            emit(Result.Error(DataError.Remote.SERVER))

                                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                    }
                                }

                            HttpURLConnection.HTTP_UNAUTHORIZED ->
                                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                            else ->
                                emit(Result.Error(DataError.Remote.UNKNOWN))
                        }
                    }
            }
        }.catch {
            Timber.e("getAdminStatistics() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
}

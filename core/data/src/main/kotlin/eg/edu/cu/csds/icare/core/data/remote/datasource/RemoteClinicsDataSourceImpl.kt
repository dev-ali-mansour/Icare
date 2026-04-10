package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicianDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorScheduleDto
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
class RemoteClinicsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteClinicsDataSource {
    override fun fetchClinics(): Flow<RequestState<List<ClinicDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    val response = service.fetchClinics(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(res.clinics))
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
        }.catch {
            Timber.e("fetchClinics() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewClinic(clinic: ClinicDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinic(clinic.copy(token = token))
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
            Timber.e("addNewClinic() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updateClinic(clinic: ClinicDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinic(clinic.copy(token = token))
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
            Timber.e("updateClinic() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun fetchDoctors(): Flow<RequestState<List<DoctorDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    val response = service.fetchDoctors(map)
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK -> {
                                        emit(RequestState.Success(res.doctors))
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
                } ?: run {
                emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("fetchDoctors() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewDoctor(doctor: DoctorDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertDoctor(doctor.copy(token = token))
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
            Timber.e("addNewDoctor() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updateDoctor(doctor: DoctorDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertDoctor(doctor.copy(token = token))
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
            Timber.e("updateDoctor() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun getDoctorSchedule(uid: String?): Flow<RequestState<DoctorScheduleDto, DataError.Remote>> =
        flow {
            auth.currentUser?.let { currentUser ->
                currentUser
                    .getIdToken(false)
                    .await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        map["uid"] = uid ?: currentUser.uid
                        val response = service.getDoctorSchedule(map)
                        when (response.code()) {
                            HTTP_OK -> {
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK -> {
                                            emit(RequestState.Success(res.schedule))
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
            } ?: run {
                emit(RequestState.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("getDoctorSchedule() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun listClinicians(): Flow<RequestState<List<ClinicianDto>, DataError.Remote>> =
        flow {
            auth.currentUser?.let {
                auth.currentUser
                    ?.getIdToken(false)
                    ?.await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        val response = service.listClinicians(map)
                        when (response.code()) {
                            HTTP_OK -> {
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK -> {
                                            emit(RequestState.Success(res.clinicians))
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
            }
        }.catch {
            Timber.e("listClinicians() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun addNewClinician(clinician: ClinicianDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinician(clinician.copy(token = token))
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
            Timber.e("addNewClinician() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }

    override fun updateClinician(clinician: ClinicianDto): Flow<RequestState<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinician(clinician.copy(token = token))
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
            Timber.e("updateClinician() error ${it.javaClass.simpleName}: ${it.message}")
            emit(RequestState.Error(it.toRemoteError()))
        }
}

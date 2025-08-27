package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicianDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorScheduleDto
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
class RemoteClinicsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteClinicsDataSource {
    override fun fetchClinics(): Flow<Result<List<ClinicDto>, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.clinics))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }
                        }

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("fetchClinics() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun addNewClinic(clinic: ClinicDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinic(clinic.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
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

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("addNewClinic() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun updateClinic(clinic: ClinicDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinic(clinic.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
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

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("updateClinic() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun fetchDoctors(): Flow<Result<List<DoctorDto>, DataError.Remote>> =
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
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.doctors))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Result.Error(DataError.Remote.SERVER))
                                }
                            }
                        }

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("fetchDoctors() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun addNewDoctor(doctor: DoctorDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertDoctor(doctor.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
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
            Timber.e("addNewDoctor() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun updateDoctor(doctor: DoctorDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertDoctor(doctor.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
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
            Timber.e("updateDoctor() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun getDoctorSchedule(): Flow<Result<DoctorScheduleDto, DataError.Remote>> =
        flow {
            auth.currentUser?.let { currentUser ->
                currentUser
                    .getIdToken(false)
                    .await()
                    ?.token
                    ?.let { token ->
                        val map = HashMap<String, String>()
                        map["token"] = token
                        map["uid"] = currentUser.uid
                        val response = service.getDoctorSchedule(map)
                        when (response.code()) {
                            HTTP_OK -> {
                                response.body()?.let { res ->
                                    when (res.statusCode) {
                                        Constants.ERROR_CODE_OK ->
                                            emit(Result.Success(res.schedule))

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
            } ?: run {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }.catch {
            Timber.e("getDoctorSchedule() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun listClinicians(): Flow<Result<List<ClinicianDto>, DataError.Remote>> =
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
                                        Constants.ERROR_CODE_OK ->
                                            emit(Result.Success(res.clinicians))

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
            }
        }.catch {
            Timber.e("listClinicians() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun addNewClinician(clinician: ClinicianDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinician(clinician.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("addNewClinician() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }

    override fun updateClinician(clinician: ClinicianDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertClinician(clinician.copy(token = token))
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(Unit))

                                    Constants.ERROR_CODE_EXPIRED_TOKEN ->
                                        emit(Result.Error(DataError.Remote.ACCESS_TOKEN_EXPIRED))

                                    else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                                }
                            }

                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))

                        else -> emit(Result.Error(DataError.Remote.UNKNOWN))
                    }
                }
        }.catch {
            Timber.e("updateClinician() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(it.toRemoteError()))
        }
}

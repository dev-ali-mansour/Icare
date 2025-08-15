package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.dto.ConsultationDto
import eg.edu.cu.csds.icare.core.data.dto.MedicalRecordDto
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
class RemoteConsultationsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteConsultationsDataSource {
    override fun addNewConsultation(consultation: ConsultationDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertConsultation(consultation.copy(token = token))
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
        }.catch {
            Timber.e("addNewConsultation() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun updateConsultation(consultation: ConsultationDto): Flow<Result<Unit, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val response = service.upsertConsultation(consultation.copy(token = token))
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
        }.catch {
            Timber.e("updateConsultation() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getMedicalRecord(patientId: String): Flow<Result<MedicalRecordDto, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    map["uid"] = patientId
                    val response = service.getMedicalRecord(map)
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(
                                            Result.Success(res.medicalRecord),
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
            Timber.e("getMedicalRecord() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getMedicationsByStatus(
        statusId: Short,
    ): Flow<Result<List<ConsultationDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    map["status"] = statusId.toString()
                    val response = service.getMedicationsByStatus(map)
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.consultations))

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
            Timber.e("getMedicationsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getLabTestsByStatus(statusId: Short): Flow<Result<List<ConsultationDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    map["status"] = statusId.toString()
                    val response = service.getLabTestsByStatus(map)
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.consultations))

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
            Timber.e("getLabTestsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }

    override fun getImagingTestsByStatus(
        statusId: Short,
    ): Flow<Result<List<ConsultationDto>, DataError.Remote>> =
        flow {
            auth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?.let { token ->
                    val map = HashMap<String, String>()
                    map["token"] = token
                    map["status"] = statusId.toString()
                    val response = service.getImagingTestsByStatus(map)
                    when (response.code()) {
                        HTTP_OK ->
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Result.Success(res.consultations))

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
            Timber.e("getImagingTestsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
}

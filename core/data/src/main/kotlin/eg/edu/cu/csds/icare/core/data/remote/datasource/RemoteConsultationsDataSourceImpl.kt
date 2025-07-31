package eg.edu.cu.csds.icare.core.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthorizedException
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.data.remote.serivce.ApiService
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
class RemoteConsultationsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteConsultationsDataSource {
    override fun addNewConsultation(consultation: Consultation): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }
            val response = service.upsertConsultation(consultation.copy(token = token))
            when (response.code()) {
                HTTP_OK ->
                    response.body()?.let { res ->
                        when (res.statusCode) {
                            Constants.ERROR_CODE_OK -> {
                                emit(Resource.Success(null))
                            }

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
            Timber.e("addNewConsultation() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updateConsultation(consultation: Consultation): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val token =
                runBlocking {
                    auth.currentUser
                        ?.getIdToken(false)
                        ?.await()
                        ?.token
                        .toString()
                }
            val response = service.upsertConsultation(consultation.copy(token = token))
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
            Timber.e("updateConsultation() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun getMedicalRecord(patientId: String): Flow<Resource<MedicalRecord>> =
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
                map["uid"] = patientId
                val response = service.getMedicalRecord(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.medicalRecord))

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
        }.catch {
            Timber.e("getMedicalRecord() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun getMedicationsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
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
                map["status"] = statusId.toString()
                val response = service.getMedicationsByStatus(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.consultations))

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
        }.catch {
            Timber.e("getMedicationsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun getLabTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
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
                map["status"] = statusId.toString()
                val response = service.getLabTestsByStatus(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.consultations))

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
        }.catch {
            Timber.e("getLabTestsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun getImagingTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
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
                map["status"] = statusId.toString()
                val response = service.getImagingTestsByStatus(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.consultations))

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
        }.catch {
            Timber.e("getImagingTestsByStatus() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }
}

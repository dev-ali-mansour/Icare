package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
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
class RemotePharmaciesDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemotePharmaciesDataSource {
    override fun fetchPharmacies(): Flow<Resource<List<Pharmacy>>> =
        flow {
            emit(Resource.Loading())
            auth.currentUser?.let {
                val response = service.fetchPharmacies()
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.pharmacies))

                                Constants.ERROR_CODE_SERVER_ERROR ->
                                    emit(Resource.Error(ConnectException()))
                            }
                        }
                    }

                    else -> {
                        // Todo Pass ConnectException While failing to connect to the server
                        emit(Resource.Success(listOf()))
                        emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }
        }.catch {
            Timber.e("fetchPharmacies() error ${it.javaClass.simpleName}: ${it.message}")
            // Todo Pass ConnectException While failing to connect to the server
            emit(Resource.Success(listOf()))
//            emit(Resource.Error(it))
        }

    override fun addNewPharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.addNewPharmacy(pharmacy)
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
            Timber.e("addNewPharmacy() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updatePharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.updatePharmacy(pharmacy)
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
            Timber.e("updatePharmacy() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun listPharmacists(pharmacyId: Long): Flow<Resource<List<Pharmacist>>> =
        flow {
            emit(Resource.Loading())
            auth.currentUser?.let {
                val map = HashMap<String, String>()
                map["pharmacyId"] = pharmacyId.toString()
                val response = service.listPharmacists(map)
                when (response.code()) {
                    HTTP_OK -> {
                        response.body()?.let { res ->
                            when (res.statusCode) {
                                Constants.ERROR_CODE_OK ->
                                    emit(Resource.Success(res.pharmacists))

                                Constants.ERROR_CODE_SERVER_ERROR ->
                                    emit(Resource.Error(ConnectException()))
                            }
                        }
                    }

                    else -> emit(Resource.Error(ConnectException(response.code().toString())))
                }
            }
        }.catch {
            Timber.e("listPharmacists() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.addNewPharmacist(pharmacist)
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
            Timber.e("addNewPharmacist() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }

    override fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> =
        flow<Resource<Nothing?>> {
            val response = service.updatePharmacist(pharmacist)
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
            Timber.e("updatePharmacist() error ${it.javaClass.simpleName}: ${it.message}")
            emit(Resource.Error(it))
        }
}

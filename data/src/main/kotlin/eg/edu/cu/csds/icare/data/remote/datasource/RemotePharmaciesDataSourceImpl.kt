package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
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
class RemotePharmaciesDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemotePharmaciesDataSource {
    override fun fetchPharmacies(): Flow<Resource<List<Pharmacy>>> =
        flow {
            runCatching {
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

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("fetchPharmacies() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewPharmacy(center: Pharmacy): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updatePharmacy(pharmacy: Pharmacy): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listPharmacists(pharmacyId: Long): Flow<Resource<List<Pharmacist>>> {
        TODO("Not yet implemented")
    }

    override fun addNewPharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updatePharmacist(pharmacist: Pharmacist): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}

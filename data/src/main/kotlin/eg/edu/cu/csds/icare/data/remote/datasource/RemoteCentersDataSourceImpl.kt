package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
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
class RemoteCentersDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteCentersDataSource {
    override fun fetchCenters(): Flow<Resource<List<LabImagingCenter>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let {
                    val response = service.fetchCenters()
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.centers))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("fetchCenters() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateCenter(center: LabImagingCenter): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listCenterStaff(centerId: Long): Flow<Resource<List<CenterStaff>>> {
        TODO("Not yet implemented")
    }

    override fun addNewCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateCenterStaff(staff: CenterStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}

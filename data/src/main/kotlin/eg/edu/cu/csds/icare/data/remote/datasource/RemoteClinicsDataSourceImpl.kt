package eg.edu.cu.csds.icare.data.remote.datasource

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
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
class RemoteClinicsDataSourceImpl(
    private val auth: FirebaseAuth,
    private val service: ApiService,
) : RemoteClinicsDataSource {
    override fun fetchClinics(): Flow<Resource<List<Clinic>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let {
                    val response = service.fetchClinics()
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.clinics))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("fetchClinics() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun fetchDoctors(): Flow<Resource<List<Doctor>>> =
        flow {
            runCatching {
                emit(Resource.Loading())
                auth.currentUser?.let {
                    val response = service.fetchDoctors()
                    when (response.code()) {
                        HTTP_OK -> {
                            response.body()?.let { res ->
                                when (res.statusCode) {
                                    Constants.ERROR_CODE_OK ->
                                        emit(Resource.Success(res.doctors))

                                    Constants.ERROR_CODE_SERVER_ERROR ->
                                        emit(Resource.Error(ConnectException()))
                                }
                            }
                        }

                        else -> emit(Resource.Error(ConnectException(response.code().toString())))
                    }
                }
            }.onFailure {
                Timber.e("fetchDoctors() error ${it.javaClass.simpleName}: ${it.message}")
                emit(Resource.Error(it))
            }
        }

    override fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listClinicStaff(clinicId: Long): Flow<Resource<List<ClinicStaff>>> {
        TODO("Not yet implemented")
    }

    override fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}

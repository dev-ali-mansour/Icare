package eg.edu.cu.csds.icare.core.data.repository

import android.content.Context
import eg.edu.cu.csds.icare.core.data.local.datasource.LocalClinicsDataSource
import eg.edu.cu.csds.icare.core.data.local.datasource.LocalDoctorDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toClinic
import eg.edu.cu.csds.icare.core.data.mappers.toClinicDto
import eg.edu.cu.csds.icare.core.data.mappers.toClinicEntity
import eg.edu.cu.csds.icare.core.data.mappers.toClinicStaff
import eg.edu.cu.csds.icare.core.data.mappers.toClinicStaffDto
import eg.edu.cu.csds.icare.core.data.mappers.toDoctor
import eg.edu.cu.csds.icare.core.data.mappers.toDoctorDto
import eg.edu.cu.csds.icare.core.data.mappers.toDoctorEntity
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteClinicsDataSource
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class ClinicsRepositoryImpl(
    private val context: Context,
    private val remoteClinicsDataSource: RemoteClinicsDataSource,
    private val localClinicsDataSource: LocalClinicsDataSource,
    private val localDoctorDataSource: LocalDoctorDataSource,
) : ClinicsRepository {
    override fun listClinics(forceRefresh: Boolean): Flow<Resource<List<Clinic>>> =
        flow {
            if (!forceRefresh) {
                localClinicsDataSource
                    .listClinics()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toClinic() }))
                    }
                return@flow
            }
            remoteClinicsDataSource.fetchClinics().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { clinics ->
                            localClinicsDataSource.persistClinics(clinics.map { it.toClinicEntity() })
                        }
                        localClinicsDataSource
                            .listClinics()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toClinic() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remoteClinicsDataSource.addNewClinic(clinic.toClinicDto()).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listClinics(true).collect { refreshResult ->
                            when (refreshResult) {
                                is Resource.Success -> emit(Resource.Success(null))
                                is Resource.Error -> emit(Resource.Error(refreshResult.error))
                                else -> {}
                            }
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remoteClinicsDataSource.updateClinic(clinic.toClinicDto()).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listClinics(true).collect { refreshResult ->
                            when (refreshResult) {
                                is Resource.Success -> emit(Resource.Success(null))
                                is Resource.Error -> emit(Resource.Error(refreshResult.error))
                                else -> {}
                            }
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun listDoctors(forceRefresh: Boolean): Flow<Resource<List<Doctor>>> =
        flow {
            if (!forceRefresh) {
                localDoctorDataSource
                    .listDoctors()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toDoctor(context) }))
                    }
                return@flow
            }
            remoteClinicsDataSource.fetchDoctors().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { doctors ->
                            localDoctorDataSource.persistDoctors(doctors.map { it.toDoctorEntity() })
                        }
                        localDoctorDataSource
                            .listDoctors()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toDoctor(context) }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun listClinicDoctors(clinicId: Long): Flow<Resource<List<Doctor>>> =
        flow {
            localDoctorDataSource
                .listDoctors(clinicId)
                .distinctUntilChanged()
                .collect { entities ->
                    emit(Resource.Success(data = entities.map { it.toDoctor(context) }))
                }
        }

    override fun getDoctorSchedule(uid: String): Flow<Resource<DoctorSchedule>> =
        remoteClinicsDataSource.getDoctorSchedule(
            uid,
        )

    override fun listTopDoctors(): Flow<Resource<List<Doctor>>> =
        flow {
            localDoctorDataSource
                .listTopDoctors()
                .distinctUntilChanged()
                .collect { entities ->
                    emit(Resource.Success(data = entities.map { it.toDoctor(context) }))
                }
        }

    override fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remoteClinicsDataSource.addNewDoctor(doctor.toDoctorDto()).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listDoctors(true).collect { refreshResult ->
                            when (refreshResult) {
                                is Resource.Success -> emit(Resource.Success(null))
                                is Resource.Error -> emit(Resource.Error(refreshResult.error))
                                else -> {}
                            }
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>> =
        flow {
            emit(Resource.Loading())
            remoteClinicsDataSource.updateDoctor(doctor.toDoctorDto()).collect { res ->
                when (res) {
                    is Resource.Unspecified<*> -> emit(Resource.Unspecified())
                    is Resource.Loading<*> -> emit(Resource.Loading())
                    is Resource.Success ->
                        listDoctors(true).collect { refreshResult ->
                            when (refreshResult) {
                                is Resource.Success -> emit(Resource.Success(null))
                                is Resource.Error -> emit(Resource.Error(refreshResult.error))
                                else -> {}
                            }
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun listClinicStaff(): Flow<Resource<List<ClinicStaff>>> =
        flow {
            remoteClinicsDataSource.listClinicStaff().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toClinicStaff() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> =
        remoteClinicsDataSource.updateClinicStaff(
            staff.toClinicStaffDto(),
        )

    override fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> =
        remoteClinicsDataSource.updateClinicStaff(
            staff.toClinicStaffDto(),
        )
}

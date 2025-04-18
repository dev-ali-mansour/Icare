package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalClinicsDataSource
import eg.edu.cu.csds.icare.data.local.datasource.LocalDoctorDataSource
import eg.edu.cu.csds.icare.data.local.db.entity.toEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toModel
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteClinicsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class ClinicsRepositoryImpl(
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
                        emit(Resource.Success(data = entities.map { it.toModel() }))
                    }
                return@flow
            }
            remoteClinicsDataSource.fetchClinics().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { clinics ->
                            localClinicsDataSource.persistClinics(clinics.map { it.toEntity() })
                        }
                        localClinicsDataSource
                            .listClinics()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toModel() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>> = remoteClinicsDataSource.addNewClinic(clinic)

    override fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>> = remoteClinicsDataSource.updateClinic(clinic)

    override fun listDoctors(forceRefresh: Boolean): Flow<Resource<List<Doctor>>> =
        flow {
            if (!forceRefresh) {
                localDoctorDataSource
                    .listDoctors()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Resource.Success(data = entities.map { it.toModel() }))
                    }
                return@flow
            }
            remoteClinicsDataSource.fetchDoctors().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { doctors ->
                            localDoctorDataSource.persistDoctors(doctors.map { it.toEntity() })
                        }
                        localDoctorDataSource
                            .listDoctors()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Resource.Success(data = entities.map { it.toModel() }))
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
                    emit(Resource.Success(data = entities.map { it.toModel() }))
                }
        }

    override fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>> = remoteClinicsDataSource.addNewDoctor(doctor)

    override fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>> = remoteClinicsDataSource.updateDoctor(doctor)

    override fun listClinicStaff(clinicId: Long): Flow<Resource<List<ClinicStaff>>> = remoteClinicsDataSource.listClinicStaff(clinicId)

    override fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> = remoteClinicsDataSource.updateClinicStaff(staff)

    override fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> = remoteClinicsDataSource.updateClinicStaff(staff)
}

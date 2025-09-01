package eg.edu.cu.csds.icare.core.data.repository

import android.content.Context
import eg.edu.cu.csds.icare.core.data.local.datasource.LocalClinicsDataSource
import eg.edu.cu.csds.icare.core.data.local.datasource.LocalDoctorDataSource
import eg.edu.cu.csds.icare.core.data.mappers.toClinic
import eg.edu.cu.csds.icare.core.data.mappers.toClinicDto
import eg.edu.cu.csds.icare.core.data.mappers.toClinicEntity
import eg.edu.cu.csds.icare.core.data.mappers.toClinician
import eg.edu.cu.csds.icare.core.data.mappers.toClinicianDto
import eg.edu.cu.csds.icare.core.data.mappers.toDoctor
import eg.edu.cu.csds.icare.core.data.mappers.toDoctorDto
import eg.edu.cu.csds.icare.core.data.mappers.toDoctorEntity
import eg.edu.cu.csds.icare.core.data.mappers.toDoctorSchedule
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteClinicsDataSource
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class ClinicsRepositoryImpl(
    private val context: Context,
    private val remoteClinicsDataSource: RemoteClinicsDataSource,
    private val localClinicsDataSource: LocalClinicsDataSource,
    private val localDoctorDataSource: LocalDoctorDataSource,
) : ClinicsRepository {
    override fun listClinics(forceUpdate: Boolean): Flow<Result<List<Clinic>, DataError.Remote>> =
        flow {
            Timber
                .tag("ClinicsRepositoryImpl")
                .d("listClinics called with forceRefresh: $forceUpdate")
            if (!forceUpdate) {
                localClinicsDataSource
                    .listClinics()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Result.Success(data = entities.map { it.toClinic() }))
                    }
                return@flow
            }
            remoteClinicsDataSource.fetchClinics().collect { result ->
                result
                    .onSuccess { clinics ->
                        localClinicsDataSource.persistClinics(clinics.map { it.toClinicEntity() })
                        localClinicsDataSource
                            .listClinics()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Result.Success(data = entities.map { it.toClinic() }))
                            }
                    }.onError { emit(Result.Error(it)) }
            }
        }

    override fun addNewClinic(clinic: Clinic): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remoteClinicsDataSource
                .addNewClinic(clinic.toClinicDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listClinics(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun updateClinic(clinic: Clinic): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remoteClinicsDataSource
                .updateClinic(clinic.toClinicDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listClinics(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun listDoctors(forceUpdate: Boolean): Flow<Result<List<Doctor>, DataError.Remote>> =
        flow {
            if (!forceUpdate) {
                localDoctorDataSource
                    .listDoctors()
                    .distinctUntilChanged()
                    .collect { entities ->
                        emit(Result.Success(data = entities.map { it.toDoctor(context) }))
                    }
                return@flow
            }
            remoteClinicsDataSource.fetchDoctors().collect { result ->
                result
                    .onSuccess { doctors ->
                        localDoctorDataSource.persistDoctors(doctors.map { it.toDoctorEntity() })
                        localDoctorDataSource
                            .listDoctors()
                            .distinctUntilChanged()
                            .collect { entities ->
                                emit(Result.Success(data = entities.map { it.toDoctor(context) }))
                            }
                    }.onError { emit(Result.Error(it)) }
            }
        }

    override fun getCurrentDoctor(): Flow<Result<Doctor, DataError.Remote>> =
        flow {
            runCatching {
                localDoctorDataSource.getCurrentDoctor()?.let { doctorEntity ->
                    emit(Result.Success(data = doctorEntity.toDoctor(context)))
                } ?: run {
                    emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
                }
            }.onFailure {
                emit(Result.Error(DataError.Remote.USER_NOT_AUTHORIZED))
            }
        }

    override fun listClinicDoctors(clinicId: Long): Flow<Result<List<Doctor>, DataError.Remote>> =
        flow {
            localDoctorDataSource
                .listDoctors(clinicId)
                .distinctUntilChanged()
                .collect { entities ->
                    emit(Result.Success(data = entities.map { it.toDoctor(context) }))
                }
        }

    override fun getDoctorSchedule(uid: String?): Flow<Result<DoctorSchedule, DataError.Remote>> =
        flow {
            remoteClinicsDataSource
                .getDoctorSchedule(uid)
                .collect { result ->
                    result
                        .onSuccess { scheduleDto ->
                            emit(Result.Success(data = scheduleDto.toDoctorSchedule()))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun listTopDoctors(): Flow<Result<List<Doctor>, DataError.Remote>> =
        flow {
            localDoctorDataSource
                .listTopDoctors()
                .distinctUntilChanged()
                .collect { entities ->
                    emit(Result.Success(data = entities.map { it.toDoctor(context) }))
                }
        }

    override fun addNewDoctor(doctor: Doctor): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remoteClinicsDataSource
                .addNewDoctor(doctor.toDoctorDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listDoctors(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun updateDoctor(doctor: Doctor): Flow<Result<Unit, DataError.Remote>> =
        flow {
            remoteClinicsDataSource
                .updateDoctor(doctor.toDoctorDto())
                .collect { result ->
                    result
                        .onSuccess {
                            listDoctors(forceUpdate = true).collect { listResult ->
                                listResult
                                    .onSuccess { emit(Result.Success(Unit)) }
                                    .onError { emit(Result.Error(it)) }
                            }
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun listClinicians(): Flow<Result<List<Clinician>, DataError.Remote>> =
        flow {
            remoteClinicsDataSource
                .listClinicians()
                .collect { result ->
                    result
                        .onSuccess {
                            emit(Result.Success(data = it.map { staffDto -> staffDto.toClinician() }))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun addNewClinician(clinician: Clinician): Flow<Result<Unit, DataError.Remote>> =
        remoteClinicsDataSource.updateClinician(clinician.toClinicianDto())

    override fun updateClinician(clinician: Clinician): Flow<Result<Unit, DataError.Remote>> =
        remoteClinicsDataSource.updateClinician(clinician.toClinicianDto())
}

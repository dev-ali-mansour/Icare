package eg.edu.cu.csds.icare.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.core.data.mappers.toConsultation
import eg.edu.cu.csds.icare.core.data.mappers.toConsultationDto
import eg.edu.cu.csds.icare.core.data.mappers.toMedicalRecord
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteConsultationsDataSource
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class ConsultationsRepositoryImpl(
    private val auth: FirebaseAuth,
    private val remoteConsultationsDataSource: RemoteConsultationsDataSource,
) : ConsultationsRepository {
    override fun addNewConsultation(consultation: Consultation): Flow<Result<Unit, DataError.Remote>> =
        remoteConsultationsDataSource.addNewConsultation(consultation.toConsultationDto())

    override fun updateConsultation(consultation: Consultation): Flow<Result<Unit, DataError.Remote>> =
        remoteConsultationsDataSource.updateConsultation(consultation.toConsultationDto())

    override fun getMedicalRecord(patientId: String): Flow<Result<MedicalRecord, DataError.Remote>> =
        flow {
            remoteConsultationsDataSource
                .getMedicalRecord(patientId)
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(Result.Success(data = entities.toMedicalRecord(auth.currentUser?.uid)))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun getMedicationsByStatus(
        statusId: Short,
    ): Flow<Result<List<Consultation>, DataError.Remote>> =
        flow {
            remoteConsultationsDataSource
                .getMedicationsByStatus(statusId)
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(
                                Result.Success(
                                    data = entities.map { it.toConsultation(auth.currentUser?.uid) },
                                ),
                            )
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun getLabTestsByStatus(statusId: Short): Flow<Result<List<Consultation>, DataError.Remote>> =
        flow {
            remoteConsultationsDataSource
                .getLabTestsByStatus(statusId)
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(
                                Result.Success(
                                    data = entities.map { it.toConsultation(auth.currentUser?.uid) },
                                ),
                            )
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun getImagingTestsByStatus(
        statusId: Short,
    ): Flow<Result<List<Consultation>, DataError.Remote>> =
        flow {
            remoteConsultationsDataSource
                .getImagingTestsByStatus(statusId)
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(
                                Result.Success(
                                    data = entities.map { it.toConsultation(auth.currentUser?.uid) },
                                ),
                            )
                        }.onError { emit(Result.Error(it)) }
                }
        }
}

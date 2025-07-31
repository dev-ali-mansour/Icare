package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.mappers.toConsultation
import eg.edu.cu.csds.icare.core.data.mappers.toConsultationDto
import eg.edu.cu.csds.icare.core.data.mappers.toMedicalRecord
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteConsultationsDataSource
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class ConsultationsRepositoryImpl(
    private val remoteConsultationsDataSource: RemoteConsultationsDataSource,
) : ConsultationsRepository {
    override fun addNewConsultation(consultation: Consultation): Flow<Resource<Nothing?>> =
        remoteConsultationsDataSource.addNewConsultation(consultation.toConsultationDto())

    override fun updateConsultation(consultation: Consultation): Flow<Resource<Nothing?>> =
        remoteConsultationsDataSource.updateConsultation(consultation.toConsultationDto())

    override fun getMedicalRecord(patientId: String): Flow<Resource<MedicalRecord>> =
        flow {
            remoteConsultationsDataSource.getMedicalRecord(patientId).collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.toMedicalRecord()))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun getMedicationsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
        flow {
            remoteConsultationsDataSource.getMedicationsByStatus(statusId).collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toConsultation() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun getLabTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
        flow {
            remoteConsultationsDataSource.getLabTestsByStatus(statusId).collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toConsultation() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun getImagingTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
        flow {
            remoteConsultationsDataSource.getImagingTestsByStatus(statusId).collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toConsultation() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }
}

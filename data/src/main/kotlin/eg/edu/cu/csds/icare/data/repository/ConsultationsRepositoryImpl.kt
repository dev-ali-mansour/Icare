package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ConsultationsRepository
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteConsultationsDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ConsultationsRepositoryImpl(
    private val remoteConsultationsDataSource: RemoteConsultationsDataSource,
) : ConsultationsRepository {
    override fun addNewConsultation(consultation: Consultation): Flow<Resource<Nothing?>> =
        remoteConsultationsDataSource.addNewConsultation(consultation)

    override fun updateConsultation(consultation: Consultation): Flow<Resource<Nothing?>> =
        remoteConsultationsDataSource.updateConsultation(consultation)

    override fun getMedicalRecord(patientId: String): Flow<Resource<MedicalRecord>> =
        remoteConsultationsDataSource.getMedicalRecord(patientId)

    override fun getMedicationsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
        remoteConsultationsDataSource.getMedicationsByStatus(statusId)

    override fun getLabTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
        remoteConsultationsDataSource.getLabTestsByStatus(statusId)

    override fun getImagingTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>> =
        remoteConsultationsDataSource.getImagingTestsByStatus(statusId)
}

package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.ConsultationDto
import eg.edu.cu.csds.icare.core.data.dto.MedicalRecordDto
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteConsultationsDataSource {
    fun addNewConsultation(consultation: ConsultationDto): Flow<Resource<Nothing?>>

    fun updateConsultation(consultation: ConsultationDto): Flow<Resource<Nothing?>>

    fun getMedicalRecord(patientId: String): Flow<Resource<MedicalRecordDto>>

    fun getMedicationsByStatus(statusId: Short): Flow<Resource<List<ConsultationDto>>>

    fun getLabTestsByStatus(statusId: Short): Flow<Resource<List<ConsultationDto>>>

    fun getImagingTestsByStatus(statusId: Short): Flow<Resource<List<ConsultationDto>>>
}

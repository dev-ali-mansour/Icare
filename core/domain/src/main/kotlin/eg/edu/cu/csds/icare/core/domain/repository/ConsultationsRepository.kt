package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ConsultationsRepository {
    fun addNewConsultation(consultation: Consultation): Flow<Resource<Nothing?>>

    fun updateConsultation(consultation: Consultation): Flow<Resource<Nothing?>>

    fun getMedicalRecord(patientId: String): Flow<Resource<MedicalRecord>>

    fun getMedicationsByStatus(statusId: Short): Flow<Resource<List<Consultation>>>

    fun getLabTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>>

    fun getImagingTestsByStatus(statusId: Short): Flow<Resource<List<Consultation>>>
}
